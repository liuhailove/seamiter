package com.shopee.seamiter.controller;

import com.shopee.seamiter.dao.*;
import com.shopee.seamiter.domain.*;
import com.shopee.seamiter.service.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 远程API controller
 *
 * @author honggang.liu
 */
@RestController
@RequestMapping(value = "/api")
public class RemoteApiController {

    /**
     * 规则推送服务
     */
    @Resource
    private RulePublishService rulePublishService;

    /**
     * 降级规则
     */
    @Resource
    private DegradeRuleDao degradeRuleDao;

    /**
     * 流控规则
     */
    @Resource
    private FlowRuleDao flowRuleDao;

    /**
     * 热点参数规则DAO
     */
    @Resource
    private ParamFlowRuleDao paramFlowRuleDao;

    /**
     * 热点参数Item Dao
     */
    @Resource
    private ParamFlowItemDao paramFlowItemDao;

    /**
     * mock规则DAO
     */
    @Resource
    private MockRuleDao mockRuleDao;

    /**
     * mockItem规则项DAO
     */
    @Resource
    private MockItemDao mockItemDao;

    /**
     * 附加参数DAO
     */
    @Resource
    private AdditionalItemDao additionalItemDao;

    /**
     * 系统规则DAO
     */
    @Resource
    private SystemRuleDao systemRuleDao;

    /**
     * 资源MEtric接口
     */
    @Resource
    private ResourceMetricService resourceMetricService;

    /**
     * 资源rsp接口
     */
    @Resource
    private ResourceRspService resourceRspService;

    /**
     * 资源request接口
     */
    @Resource
    private ResourceRequestService resourceRequestService;

    /**
     * 重试规则Dao
     */
    @Resource
    private RetryRuleDao retryRuleDao;

    /**
     * 灰度规则服务
     */
    @Resource
    private GrayService grayService;

    /**
     * 查询应用的最新版本
     *
     * @param app             应用
     * @param hostname        机器名称
     * @param ip              ip
     * @param port            端口号
     * @param ruleTypes       规则集合
     * @param currentVersions 当前版本
     * @return 影响行数
     */
    @PostMapping("/findMaxVersion")
    public List<RulePublish> findMaxVersion(String app, String hostname, String ip, Integer port, String ruleTypes, String currentVersions) {
        QueryRuleVersionParam queryRuleVersionParam = new QueryRuleVersionParam();
        queryRuleVersionParam.setApp(app);
        queryRuleVersionParam.setHostname(hostname);
        queryRuleVersionParam.setIp(ip);
        queryRuleVersionParam.setPort(port);
        queryRuleVersionParam.setRuleTypes(ruleTypes);
        queryRuleVersionParam.setCurrentVersions(currentVersions);
        return rulePublishService.findMaxVersion(queryRuleVersionParam);
    }

    /**
     * 查询全部的降级规则
     *
     * @param app 根据APP查询降级规则
     * @return 降级规则
     */
    @PostMapping("/queryAllDegradeRule")
    public List<DegradeRule> queryAllDegradeRule(@RequestParam String app) {
        return degradeRuleDao.queryByApp(app, true);
    }

    /**
     * 查询全部的降级规则
     *
     * @param app 根据APP查询降级规则
     * @return 降级规则
     */
    @PostMapping("/queryAllFlowRule")
    public List<FlowRule> queryAllFlowRule(@RequestParam String app) {
        return flowRuleDao.queryByApp(app, true);
    }

    /**
     * 查询全部的降级规则
     *
     * @param app 根据APP查询降级规则
     * @return 降级规则
     */
    @PostMapping("/queryAllParamFlowRule")
    public List<ParamFlowRule> queryAllParamFlowRule(@RequestParam String app) {
        List<ParamFlowRule> paramFlowRules = paramFlowRuleDao.queryByApp(app, true);
        if (!CollectionUtils.isEmpty(paramFlowRules)) {
            for (ParamFlowRule paramFlowRule : paramFlowRules) {
                paramFlowRule.setParamFlowItemList(paramFlowItemDao.listAll(Long.parseLong(paramFlowRule.getId()), true));
            }
        }
        return paramFlowRules;
    }

    /**
     * 查询全部的Mock规则
     *
     * @param app 根据APP查询Mock规则
     * @return 规则
     */
    @PostMapping("/queryAllMockRule")
    public List<MockRule> queryAllMockRule(@RequestParam String app) {
        // 规则为空时也需要推送，以便清空现有的规则
        List<MockRule> mockRules = mockRuleDao.queryByApp(app, true);
        if (!CollectionUtils.isEmpty(mockRules)) {
            for (MockRule mockRule : mockRules) {
                // 设置例外配置
                mockRule.setSpecificItems(mockItemDao.listAllOpen(Long.parseLong(mockRule.getId()), true));
                // 设置附加参数
                mockRule.setAdditionalItems(additionalItemDao.listAll(Long.parseLong(mockRule.getId())));
            }
        }
        return mockRules;
    }

    /**
     * 查询全部的系统规则
     *
     * @param app 根据APP查询系统规则
     * @return 规则
     */
    @PostMapping("/queryAllSystemRule")
    public List<SystemRule> queryAllSystemRule(@RequestParam String app) {
        // 规则为空时也需要推送，以便清空现有的规则
        return systemRuleDao.queryByApp(app, true);
    }

    /**
     * 查询全部的授权规则
     *
     * @param app 根据APP查询授权规则
     * @return 规则
     */
    @PostMapping("/queryAllAuthorityRule")
    public List<?> queryAllAuthorityRule(@RequestParam String app) {
        // 规则为空时也需要推送，以便清空现有的规则
        return new ArrayList<>(0);
    }


    /**
     * 查询全部的重试规则
     *
     * @param app 根据APP查询重试规则
     * @return 重试规则
     */
    @PostMapping("/queryAllRetryRule")
    public List<RetryRuleView> queryAllRetryRule(@RequestParam String app) {
        List<RetryRuleView> retryRuleViews = new ArrayList<>();
        List<RetryRule> retryRules = retryRuleDao.queryByApp(app, true);
        if (CollectionUtils.isEmpty(retryRules)) {
            return retryRuleViews;
        }
        for (RetryRule retryRule : retryRules) {
            retryRuleViews.add(new RetryRuleView(retryRule));
        }
        return retryRuleViews;
    }


    /**
     * 查询全部的灰度规则
     *
     * @param app 根据APP查询重试规则
     * @return 重试规则
     */
    @PostMapping("/queryAllGrayRule")
    public List<GrayRule> queryAllGrayRule(@RequestParam String app) {
        return grayService.queryAllActiveGrayRule(app);
    }

    /**
     * 处理metrics
     *
     * @param app      应用名称
     * @param hostname 主机名称
     * @param ip       ip
     * @param port     端口号
     * @param metrics  metrics
     * @return 返沪处理结果
     */
    @PostMapping("/receiveMetrics")
    public String receiveMetrics(String app, String hostname, String ip, Integer port, String metrics) {
        resourceMetricService.handleMetrics(app, hostname, ip, port, metrics);
        return "success";
    }

    /**
     * 接受接口响应
     *
     * @param app      应用名称
     * @param hostname 主机名称
     * @param ip       ip
     * @param port     端口号
     * @param rsp      响应
     * @return 规则
     */
    @PostMapping("/receiveRsp")
    public String receiveRsp(String app, String hostname, String ip, Integer port, String rsp) {
        resourceRspService.handleRsp(app, hostname, ip, port, rsp);
        return "success";
    }

    /**
     * 上报请求集合
     *
     * @param app      应用名称
     * @param hostname 主机名称
     * @param ip       ip
     * @param port     端口号
     * @param reps     请求集合
     * @return 规则
     */
    @PostMapping("/receiveRequest")
    public String receiveRequest(String app, String hostname, String ip, Integer port, String reps) {
        resourceRequestService.handleRequest(app, hostname, ip, port, reps);
        return "success";
    }
}

