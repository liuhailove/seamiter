//package com.shopee.seamiter.core.rule;
//
//import com.shopee.seamiter.config.RegistryConfig;
//import com.shopee.seamiter.config.RuleType;
//import com.shopee.seamiter.core.client.ApiClient;
//import com.shopee.seamiter.core.concurrent.NamedThreadFactory;
//import com.shopee.seamiter.dao.*;
//import com.shopee.seamiter.domain.MachineInfo;
//import com.shopee.seamiter.domain.MockRule;
//import com.shopee.seamiter.domain.PushRuleLock;
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
// * mock规则推送
// *
// * @author honggang.liu
// */
//@Component
//public class MockRulesPusher {
//    /**
//     * LOGGER
//     */
//    private static final Logger LOGGER = LoggerFactory.getLogger(MockRulesPusher.class.getSimpleName());
//
//    /**
//     * 间隔
//     */
//    private static final long INTERVAL_MS = 30000;
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
//     * Mock规则DAO
//     */
//    @Resource
//    private MockRuleDao mockRuleDao;
//
//    /**
//     * Mock Item Dao
//     */
//    @Resource
//    private MockItemDao mockItemDao;
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
//    private ScheduledExecutorService mockRulesPusherScheduleService = Executors.newScheduledThreadPool(1, new NamedThreadFactory("mock-rules-pusher", true));
//
//    /**
//     * 启动pusher
//     */
//    MockRulesPusher() {
//        mockRulesPusherScheduleService.scheduleAtFixedRate(() -> {
//            try {
//                pushAllApp();
//            } catch (Exception e) {
//                LOGGER.info("mock rule push error:", e);
//            }
//        }, 5000, INTERVAL_MS, TimeUnit.MILLISECONDS);
//    }
//
//    /**
//     * 遍历全部APP，然后把流控配置推送下去
//     */
//    private void pushAllApp() {
//        PushRuleLock pushRuleLock = pushRuleLockDao.query(RuleType.MOCK_RULE_TYPE, System.currentTimeMillis());
//        if (pushRuleLock == null) {
//            return;
//        }
//        long triggerNextTime = Math.max(pushRuleLock.getTriggerNextTime() + INTERVAL_MS * 6, System.currentTimeMillis());
//        LOGGER.info("mock rule push,time={}", DateUtil.formatDateTime(new Date(triggerNextTime)));
//        int ret = pushRuleLockDao.update(RuleType.MOCK_RULE_TYPE, pushRuleLock.getTriggerNextTime(), triggerNextTime, System.currentTimeMillis());
//        if (ret <= 0) {
//            return;
//        }
//        List<String> apps = appInfoDao.findAllOnline();
//        if (CollectionUtils.isEmpty(apps)) {
//            return;
//        }
//        for (String app : apps) {
//            List<MachineInfo> machineInfos = machineInfoDao.findHealthy(app, DateUtils.addSeconds(new Date(), RegistryConfig.DEAD_TIMEOUT * (-1)).getTime());
//            LOGGER.info("mock enter pushOnce({}), machines.size()={},machine={}", app, machineInfos == null ? 0 : machineInfos.size(), machineInfos);
//            if (CollectionUtils.isEmpty(machineInfos)) {
//                continue;
//            }
//            List<MockRule> mockRules = mockRuleDao.queryByApp(app);
//            if (!CollectionUtils.isEmpty(mockRules)) {
//                for (MockRule mockRule : mockRules) {
//                    mockRule.setSpecificItems(mockItemDao.listAll(Long.parseLong(mockRule.getId())));
//                }
//            }
//            for (MachineInfo machineInfo : machineInfos) {
//                apiClient.setMockRuleOfMachine(app, machineInfo.getIp(), machineInfo.getPort(), mockRules);
//            }
//        }
//    }
//}
