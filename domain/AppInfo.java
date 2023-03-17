package com.shopee.seamiter.domain;

import com.shopee.seamiter.config.DashboardConfig;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 应用信息
 *
 * @author honggang.liu
 */
public class AppInfo implements Serializable {
    /**
     * 通用
     */
    public static final Integer COMMON_APP_TYPE = 0;

    /**
     * web
     */
    public static final Integer WEB_APP_TYPE = 1;

    /**
     * rpc
     */
    public static final Integer RPC_APP_TYPE = 2;

    /**
     * api网关
     */
    public static final Integer API_GATEWAY_APP_TYPE = 3;

    /**
     * db sql
     */
    public static final Integer DB_SQL_APP_TYPE = 4;

    /**
     * cache
     */
    public static final Integer CACHE_APP_TYPE = 5;

    /**
     * mq
     */
    public static final Integer MQ_APP_TYPE = 6;


    private Long id;
    /**
     * 应用名称
     */
    private String app = "";

    /**
     * 应用类型
     */
    private Integer appType = 0;

    /**
     * 描述信息
     */
    private String remark;

    /**
     * 执行器地址列表，多地址逗号分隔(手动录入)
     */
    private String addressList;

    /**
     * 在线状态
     */
    private boolean onlineStatus;

    /**
     * 上次访问token
     */
    private String lastAccessToken;

    /**
     * 当前访问Token
     */
    private String currentAccessToken;

    /**
     * token生效日期
     */
    private Date tokenEffectiveDate;

    /**
     * 当前生效token (定时更新到生效token)
     */
    private String tokenEffective;

    /**
     * 所属用户分组
     */
    private String ugroups;

    /**
     * 告警Seatalk
     */
    private String alarmSeatalk;

    /**
     * 日报接受人
     */
    private String reportReceiver;

    /**
     * grpc端口号
     */
    private Long grpcPort;

    /**
     * 执行器地址列表(系统注册)
     */
    private List<String> registryList;

    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * http协议
     */
    private static final String HTTP_PROTO = "http://";

    /**
     * https协议
     */
    private static final String HTTPS_PROTO = "https://";

    /**
     * 分隔符
     */
    private static final String SPLIT = ":";

    public List<String> getRegistryList() {
        if (addressList != null && addressList.trim().length() > 0) {
            registryList = new ArrayList<>(Arrays.asList(addressList.split(",")));
        } else {
            registryList = new ArrayList<>(0);
        }
        return registryList;
    }

    /**
     * 获取主机列表
     *
     * @return 主机列表
     */
    public List<String> queryHostList() {
        List<String> registries = getRegistryList();
        Set<String> hosts = new LinkedHashSet<>();
        for (String registry : registries) {
            if (registry.contains(HTTP_PROTO)) {
                registry = registry.substring(HTTP_PROTO.length());
            } else if (registry.contains(HTTPS_PROTO)) {
                registry = registry.substring(HTTPS_PROTO.length());
            }
            hosts.add(registry.split(SPLIT, 2)[0]);
        }
        return new ArrayList<>(hosts);
    }


    public String getHosts() {
        List<String> registries = new ArrayList<>();
        if (addressList != null && addressList.trim().length() > 0) {
            registries = new ArrayList<>(Arrays.asList(addressList.split(",")));
        }
        Set<String> hostSet = new LinkedHashSet<>();
        for (String registry : registries) {
            if (registry.contains(HTTP_PROTO)) {
                registry = registry.substring(HTTP_PROTO.length());
            } else if (registry.contains(HTTPS_PROTO)) {
                registry = registry.substring(HTTPS_PROTO.length());
            }
            hostSet.add(registry.split(SPLIT, 2)[0]);
        }
        return StringUtils.join(hostSet, ',');
    }

    public List<String> getUGroupList() {
        if (ugroups != null && ugroups.trim().length() > 0) {
            return new ArrayList<>(Arrays.asList(ugroups.split(",")));
        }
        return new ArrayList<>(0);
    }


    private Set<MachineInfo> machines = ConcurrentHashMap.newKeySet();

    public AppInfo() {
    }

    public AppInfo(String app) {
        this.app = app;
    }

    public AppInfo(String app, Integer appType) {
        this.app = app;
        this.appType = appType;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public Integer getAppType() {
        return appType;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the current machines.
     *
     * @return a new copy of the current machines.
     */
    public Set<MachineInfo> getMachines() {
        return new HashSet<>(machines);
    }

    @Override
    public String toString() {
        return "AppInfo{" + "app='" + app + ", machines=" + machines + '}';
    }

    public boolean addMachine(MachineInfo machineInfo) {
        machines.remove(machineInfo);
        return machines.add(machineInfo);
    }

    public synchronized boolean removeMachine(String ip, int port) {
        Iterator<MachineInfo> it = machines.iterator();
        while (it.hasNext()) {
            MachineInfo machine = it.next();
            if (machine.getIp().equals(ip) && machine.getPort() == port) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public Optional<MachineInfo> getMachine(String ip, int port) {
        return machines.stream()
                .filter(e -> e.getIp().equals(ip) && e.getPort().equals(port))
                .findFirst();
    }

    private boolean heartbeatJudge(final int threshold) {
        if (machines.isEmpty()) {
            return false;
        }
        if (threshold > 0) {
            long healthyCount = machines.stream()
                    .filter(MachineInfo::isHealthy)
                    .count();
            if (healthyCount == 0) {
                // No healthy machines.
                return machines.stream()
                        .max(Comparator.comparingLong(MachineInfo::getLastHeartbeat))
                        .map(e -> System.currentTimeMillis() - e.getLastHeartbeat() < threshold)
                        .orElse(false);
            }
        }
        return true;
    }

    /**
     * Check whether current application has no healthy machines and should not be displayed.
     *
     * @return true if the application should be displayed in the sidebar, otherwise false
     */
    public boolean isShown() {
        return heartbeatJudge(DashboardConfig.getHideAppNoMachineMillis());
    }

    /**
     * Check whether current application has no healthy machines and should be removed.
     *
     * @return true if the application is dead and should be removed, otherwise false
     */
    public boolean isDead() {
        return !heartbeatJudge(DashboardConfig.getRemoveAppNoMachineMillis());
    }

    public String getAddressList() {
        return addressList;
    }

    public List<String> getAddresses() {
        if (StringUtils.isEmpty(addressList)) {
            return new ArrayList<>(0);
        }
        return Arrays.asList(addressList.split(","));
    }

    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getLastAccessToken() {
        return lastAccessToken;
    }

    public void setLastAccessToken(String lastAccessToken) {
        this.lastAccessToken = lastAccessToken;
    }

    public String getCurrentAccessToken() {
        return currentAccessToken;
    }

    public void setCurrentAccessToken(String currentAccessToken) {
        this.currentAccessToken = currentAccessToken;
    }

    public Date getTokenEffectiveDate() {
        return tokenEffectiveDate;
    }

    public void setTokenEffectiveDate(Date tokenEffectiveDate) {
        this.tokenEffectiveDate = tokenEffectiveDate;
    }

    public String getTokenEffective() {
        return tokenEffective;
    }

    public void setTokenEffective(String tokenEffective) {
        this.tokenEffective = tokenEffective;
    }

    public String getUgroups() {
        return ugroups;
    }

    public void setUgroups(String ugroups) {
        this.ugroups = ugroups;
    }

    public void setRegistryList(List<String> registryList) {
        this.registryList = registryList;
    }

    public void setMachines(Set<MachineInfo> machines) {
        this.machines = machines;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getAlarmSeatalk() {
        return alarmSeatalk;
    }

    public void setAlarmSeatalk(String alarmSeatalk) {
        this.alarmSeatalk = alarmSeatalk;
    }

    public String getReportReceiver() {
        return reportReceiver;
    }

    public void setReportReceiver(String reportReceiver) {
        this.reportReceiver = reportReceiver;
    }

    public Long getGrpcPort() {
        return grpcPort;
    }

    public void setGrpcPort(Long grpcPort) {
        this.grpcPort = grpcPort;
    }
}
