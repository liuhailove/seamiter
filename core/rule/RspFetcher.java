//package com.shopee.seamiter.core.rule;
//
//import com.shopee.seamiter.config.RegistryConfig;
//import com.shopee.seamiter.config.RuleType;
//import com.shopee.seamiter.core.client.ApiClient;
//import com.shopee.seamiter.core.concurrent.NamedThreadFactory;
//import com.shopee.seamiter.dao.AppInfoDao;
//import com.shopee.seamiter.dao.MachineInfoDao;
//import com.shopee.seamiter.dao.PushRuleLockDao;
//import com.shopee.seamiter.dao.ResourceRspDao;
//import com.shopee.seamiter.domain.AppInfo;
//import com.shopee.seamiter.domain.MachineInfo;
//import com.shopee.seamiter.domain.PushRuleLock;
//import com.shopee.seamiter.domain.ResourceRsp;
//import org.apache.commons.lang.StringUtils;
//import org.apache.commons.lang3.time.DateUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import javax.annotation.Resource;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * 临时响应Fetcher，主要是为Mock
// *
// * @author honggang.liu
// */
//@Component
//public class RspFetcher {
//
//    /**
//     * LOGGER
//     */
//    private static final Logger LOGGER = LoggerFactory.getLogger(RspFetcher.class.getSimpleName());
//
//    /**
//     * 间隔
//     */
//    private static final long INTERVAL_MS = 60000;
//
//    /**
//     * 规则推送锁DAO
//     */
//    @Resource
//    private PushRuleLockDao pushRuleLockDao;
//
//    @Resource
//    private ApiClient apiClient;
//
//    /**
//     * 应用信息DAO
//     */
//    @Resource
//    private AppInfoDao appInfoDao;
//
//    /**
//     * 应用机器信息
//     */
//    @Resource
//    private MachineInfoDao machineInfoDao;
//
//    /**
//     * 资源响应DAO
//     */
//    @Resource
//    private ResourceRspDao resourceRspDao;
//
//    /**
//     * 应用Map
//     */
//    private Map<String, Integer> appHashcodeMap = new ConcurrentHashMap<>();
//
//    @SuppressWarnings("PMD.ThreadPoolCreationRule")
//    private ScheduledExecutorService rspFetcherScheduleService = Executors.newScheduledThreadPool(1, new NamedThreadFactory("rsp-fetcher", true));
//
//    /**
//     * 启动pusher
//     */
//    RspFetcher() {
//        rspFetcherScheduleService.scheduleAtFixedRate(() -> {
//            try {
//                pushAllApp();
//            } catch (Exception e) {
//                LOGGER.info("degrade push error:", e);
//            }
//        }, 3000, INTERVAL_MS, TimeUnit.MILLISECONDS);
//    }
//
//    /**
//     * 遍历全部APP，然后抓取资源响应
//     */
//    private void pushAllApp() {
//        PushRuleLock pushRuleLock = pushRuleLockDao.query(RuleType.RSP_FETCHER_TYPE, System.currentTimeMillis());
//        if (pushRuleLock == null) {
//            return;
//        }
//        long triggerNextTime = Math.max(pushRuleLock.getTriggerNextTime() + INTERVAL_MS, System.currentTimeMillis());
//        int ret = pushRuleLockDao.update(RuleType.RSP_FETCHER_TYPE, pushRuleLock.getTriggerNextTime(), triggerNextTime, System.currentTimeMillis());
//        if (ret <= 0) {
//            return;
//        }
//        List<AppInfo> apps = appInfoDao.findAll();
//        if (CollectionUtils.isEmpty(apps)) {
//            return;
//        }
//        for (AppInfo app : apps) {
//            int hashCode = app.getAddresses().hashCode();
//            // 没有发生过变化，则不需要抓取
//            if (appHashcodeMap.containsKey(app.getApp()) && appHashcodeMap.get(app.getApp()) == hashCode) {
//                return;
//            }
//            List<MachineInfo> machineInfos = machineInfoDao.findHealthy(app.getApp(), DateUtils.addSeconds(new Date(), RegistryConfig.DEAD_TIMEOUT * (-1)).getTime());
//            LOGGER.info("rsp fetcher,app={}, machines.size()={},machine={}", app, machineInfos == null ? 0 : machineInfos.size(), machineInfos);
//            if (CollectionUtils.isEmpty(machineInfos)) {
//                continue;
//            }
//            for (MachineInfo machineInfo : machineInfos) {
//                List<ResourceRsp> resourceRspList = apiClient.fetchRsp(machineInfo.getIp(), machineInfo.getPort());
//                if (CollectionUtils.isEmpty(resourceRspList)) {
//                    continue;
//                }
//                for (ResourceRsp rsp : resourceRspList) {
//                    if (StringUtils.isEmpty(rsp.getRsp())) {
//                        continue;
//                    }
//                    rsp.setApp(app.getApp());
//                    rsp.setGmtModified(new Date());
//                    int result = resourceRspDao.update(rsp);
//                    if (result < 1) {
//                        rsp.setGmtCreate(new Date());
//                        resourceRspDao.save(rsp);
//                    }
//                }
//            }
//        }
//    }
//}
