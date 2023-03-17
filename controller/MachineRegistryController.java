package com.shopee.seamiter.controller;

import com.shopee.seamiter.domain.MachineInfo;
import com.shopee.seamiter.service.MachineDiscoveryService;
import com.shopee.seamiter.util.Result;
import com.shopee.seamiter.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.net.util.IPAddressUtil;

import javax.annotation.Resource;

/**
 * 机器注册
 *
 * @author honggang.liu
 */
@Controller
@RequestMapping(value = "/registry", produces = MediaType.APPLICATION_JSON_VALUE)
public class MachineRegistryController {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(MachineRegistryController.class.getSimpleName());

    /**
     * 服务注册发现
     */
    @Resource
    private MachineDiscoveryService machineDiscoveryService;

    /**
     * 心跳注册
     *
     * @param app      应用名称
     * @param appType  应用类型
     * @param version  版本
     * @param v        客户端版本
     * @param hostname 主机名称
     * @param ip       IP
     * @param port     端口好
     * @return 注册结果
     */
    @ResponseBody
    @RequestMapping("/machine")
    public Result<?> receiveHeartBeat(String app, @RequestParam(value = "app_type", required = false, defaultValue = "0")
            Integer appType, Long version, String v, String hostname, String ip, Integer port,
                                      @RequestParam(value = "grpc_port", required = false, defaultValue = "0") Long grpcPort) {
        if (StringUtil.isBlank(app) || app.length() > 256) {
            return Result.ofFail(-1, "invalid appName");
        }
        if (StringUtil.isBlank(ip) || ip.length() > 128) {
            return Result.ofFail(-1, "invalid ip: " + ip);
        }
        if (!IPAddressUtil.isIPv4LiteralAddress(ip) && !IPAddressUtil.isIPv6LiteralAddress(ip)) {
            return Result.ofFail(-1, "invalid ip: " + ip);
        }
        if (port == null || port < -1) {
            return Result.ofFail(-1, "invalid port");
        }
        if (hostname != null && hostname.length() > 256) {
            return Result.ofFail(-1, "hostname too long");
        }
        if (port == -1) {
            logger.warn("Receive heartbeat from {} but port not set yet", ip);
            return Result.ofFail(-1, "your port not set yet");
        }
        if (grpcPort == null) {
            grpcPort = 0L;
        }
        String seaLimiterVersion = StringUtil.isBlank(v) ? "unknown" : v;
        version = version == null ? System.currentTimeMillis() : version;
        MachineInfo machineInfo = new MachineInfo();
        machineInfo.setApp(app);
        machineInfo.setAppType(appType);
        machineInfo.setHostname(hostname);
        machineInfo.setIp(ip);
        machineInfo.setPort(port);
        machineInfo.setHeartbeatVersion(version);
        machineInfo.setLastHeartbeat(System.currentTimeMillis());
        machineInfo.setVersion(seaLimiterVersion);
        machineInfo.setGrpcPort(grpcPort);
        machineDiscoveryService.addMachine(machineInfo);
        return Result.ofSuccessMsg("success");
    }
}
