//package com.shopee.seamiter.core.rule;
//
//import com.shopee.seamiter.config.RegistryConfig;
//import com.shopee.seamiter.config.RuleType;
//import com.shopee.seamiter.core.client.ApiClient;
//import com.shopee.seamiter.core.concurrent.NamedThreadFactory;
//import com.shopee.seamiter.dao.AppInfoDao;
//import com.shopee.seamiter.dao.MachineInfoDao;
//import com.shopee.seamiter.dao.PushRuleLockDao;
//import com.shopee.seamiter.dao.SystemRuleDao;
//import com.shopee.seamiter.domain.MachineInfo;
//import com.shopee.seamiter.domain.PushRuleLock;
//import com.shopee.seamiter.domain.SystemRule;
//import com.shopee.seamiter.util.DateUtil;
//import org.apache.commons.lang3.time.DateUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import javax.annotation.Resource;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * 系统规则推送
// *
// * @author honggang.liu
// */
//@Component
//public class SystemRulesPusher {
//    /**
//     * LOGGER
//     */
//    private static final Logger LOGGER = LoggerFactory.getLogger(SystemRulesPusher.class.getSimpleName());
//
//    /**
//     * 间隔
//     */
//    private static final long INTERVAL_MS = 17000;
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
//     * 系统规则DAO
//     */
//    @Resource
//    private SystemRuleDao systemRuleDao;
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
//    @SuppressWarnings("PMD.ThreadPoolCreationRule")
//    private ScheduledExecutorService systemRulesPusherScheduleService = Executors.newScheduledThreadPool(1, new NamedThreadFactory("system-rules-pusher", true));
//
//    /**
//     * 启动pusher
//     */
//    SystemRulesPusher() {
//        systemRulesPusherScheduleService.scheduleAtFixedRate(() -> {
//            try {
//                pushAllApp();
//            } catch (Exception e) {
//                LOGGER.info("system rule push error:", e);
//            }
//        }, 7000, INTERVAL_MS, TimeUnit.MILLISECONDS);
//    }
//
//    /**
//     * 遍历全部APP，然后把流控配置推送下去
//     */
//    private void pushAllApp() {
//        PushRuleLock pushRuleLock = pushRuleLockDao.query(RuleType.SYSTEM_RULE_TYPE, System.currentTimeMillis());
//        if (pushRuleLock == null) {
//            return;
//        }
//        long triggerNextTime = Math.max(pushRuleLock.getTriggerNextTime() + INTERVAL_MS * 6, System.currentTimeMillis());
//        LOGGER.info("system rule push,time={}", DateUtil.formatDateTime(new Date(triggerNextTime)));
//        int ret = pushRuleLockDao.update(RuleType.SYSTEM_RULE_TYPE, pushRuleLock.getTriggerNextTime(), triggerNextTime, System.currentTimeMillis());
//        if (ret <= 0) {
//            return;
//        }
//        List<String> apps = appInfoDao.findAllOnline();
//        if (CollectionUtils.isEmpty(apps)) {
//            return;
//        }
//        for (String app : apps) {
//            List<MachineInfo> machineInfos = machineInfoDao.findHealthy(app, DateUtils.addSeconds(new Date(), RegistryConfig.DEAD_TIMEOUT * (-1)).getTime());
//            LOGGER.info("system enter pushOnce({}), machines.size()={},machine={}", app, machineInfos == null ? 0 : machineInfos.size(), machineInfos);
//            if (CollectionUtils.isEmpty(machineInfos)) {
//                continue;
//            }
//            List<SystemRule> systemRules = systemRuleDao.queryByApp(app);
//            for (MachineInfo machineInfo : machineInfos) {
//                apiClient.setSystemRuleOfMachine(app, machineInfo.getIp(), machineInfo.getPort(), systemRules);
//            }
//        }
//    }
//}
