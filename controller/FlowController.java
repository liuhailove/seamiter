package com.shopee.seamiter.controller;

import com.shopee.seamiter.auth.AuthAction;
import com.shopee.seamiter.auth.AuthService;
import com.shopee.seamiter.domain.FlowRule;
import com.shopee.seamiter.service.FlowRuleService;
import com.shopee.seamiter.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 流控规则
 *
 * @author honggang.liu
 */
@RestController
@RequestMapping(value = "/flow")
public class FlowController {

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(FlowController.class.getSimpleName());

    /**
     * 流控服务
     */
    @Resource
    private FlowRuleService flowRuleService;

    /**
     * 获取机器配置的流控规则
     *
     * @param page            页码
     * @param limit           分页大小
     * @param app             应用名称
     * @param resource        资源名称
     * @param grade           阈值类型
     * @param strategy        流控模式
     * @param controlBehavior 流控效果
     * @param permissionApps  授权应用
     * @return 规则配置
     */
    @GetMapping("/rules")
    public Map<String, Object> queryRules(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) String app, @RequestParam(required = false) String resource,
            @RequestParam(required = false) Integer grade, @RequestParam(required = false) Integer strategy,
            @RequestParam(required = false) Integer controlBehavior,
            @RequestParam(required = false) List<String> permissionApps) {
        // page list
        List<FlowRule> list = flowRuleService.pageList((page - 1) * limit, limit, app, resource, grade, strategy, controlBehavior, permissionApps);
        int listCount = flowRuleService.pageListCount((page - 1) * limit, limit, app, resource, grade, strategy, controlBehavior, permissionApps);
        return Result.ofMap(list, listCount);
    }

    /**
     * 流控规则保存
     *
     * @param flowRule 流控规则
     * @return 影响行数
     */
    @AuthAction(targetName = "/flow/add", value = AuthService.PrivilegeType.ALL)
    @PostMapping("/add")
    @ResponseBody
    public Result<Integer> add(@RequestBody FlowRule flowRule) {
        logger.info("FlowController.add，param:{}", flowRule);
        return flowRuleService.save(flowRule);
    }


    /**
     * 流控规则加载
     *
     * @param id 降级规则Id
     * @return 降级规则
     */
    @GetMapping("/load")
    @ResponseBody
    public Result<FlowRule> load(Long id) {
        return flowRuleService.load(id);
    }


    /**
     * 流控规则加载
     *
     * @param app      应用名称
     * @param resource 资源名称
     * @return 规则
     */
    @GetMapping("/loadBy")
    @ResponseBody
    public Result<FlowRule> loadBy(String app, String resource) {
        return flowRuleService.loadBy(app, resource);
    }

    /**
     * 移除
     *
     * @param id 降级规则ID
     * @return 影响行数
     */
    @AuthAction(targetName = "/flow/remove", value = AuthService.PrivilegeType.ALL)
    @GetMapping("/remove")
    @ResponseBody
    public Result<?> remove(Long id) {
        return flowRuleService.remove(id);
    }

    /**
     * 降级规则更新
     *
     * @param flowRule 降级规则
     * @return 影响行数
     */
    @AuthAction(targetName = "/flow/update", value = AuthService.PrivilegeType.ALL)
    @PostMapping("/update")
    @ResponseBody
    public Result<Integer> update(@RequestBody FlowRule flowRule) {
        return flowRuleService.update(flowRule);
    }
}
