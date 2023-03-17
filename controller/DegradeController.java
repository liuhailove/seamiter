package com.shopee.seamiter.controller;

import com.shopee.seamiter.auth.AuthAction;
import com.shopee.seamiter.auth.AuthService;
import com.shopee.seamiter.domain.DegradeRule;
import com.shopee.seamiter.service.DegradeRuleService;
import com.shopee.seamiter.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 降级规则
 *
 * @author honggang.liu
 */
@RestController
@RequestMapping(value = "/degrade")
public class DegradeController {

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(DegradeController.class.getSimpleName());


    /**
     * 降级服务
     */
    @Resource
    private DegradeRuleService degradeRuleService;

    /**
     * 获取机器配置的流控规则
     *
     * @param page           页码
     * @param limit          分页大小
     * @param app            应用名称
     * @param resource       资源名称
     * @param strategy       流控模式
     * @param permissionApps 授权应用
     * @return 规则配置
     */
    @GetMapping("/rules")
    public Map<String, Object> queryMachineRules(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) String app, @RequestParam(required = false) String resource,
            @RequestParam(required = false) Integer strategy,
            @RequestParam(required = false) List<String> permissionApps) {
        // page list
        List<DegradeRule> list = degradeRuleService.pageList((page - 1) * limit, limit, app, resource, strategy, permissionApps);
        int listCount = degradeRuleService.pageListCount((page - 1) * limit, limit, app, resource, strategy, permissionApps);
        return Result.ofMap(list, listCount);
    }


    /**
     * 降级规则
     *
     * @param degradeRule 降级规则
     * @return 影响行数
     */
    @AuthAction(targetName = "/degrade/add", value = AuthService.PrivilegeType.ALL)
    @PostMapping("/add")
    @ResponseBody
    public Result<Integer> add(@RequestBody DegradeRule degradeRule) {
        return degradeRuleService.add(degradeRule);
    }

    /**
     * 降级规则更新
     *
     * @param degradeRule 降级规则
     * @return 影响行数
     */
    @AuthAction(targetName = "/degrade/update", value = AuthService.PrivilegeType.ALL)
    @PostMapping("/update")
    @ResponseBody
    public Result<Integer> update(@RequestBody DegradeRule degradeRule) {
        return degradeRuleService.update(degradeRule);
    }

    /**
     * 降级规则加载
     *
     * @param id 降级规则Id
     * @return 降级规则
     */
    @GetMapping("/load")
    @ResponseBody
    public Result<DegradeRule> load(Long id) {
        return degradeRuleService.load(id);
    }


    /**
     * 降级规则加载
     *
     * @param app      应用名称
     * @param resource 资源名称
     * @return 降级规则
     */
    @GetMapping("/loadBy")
    @ResponseBody
    public Result<DegradeRule> loadBy(String app, String resource) {
        return degradeRuleService.loadBy(app, resource);
    }

    /**
     * 移除
     *
     * @param id 降级规则ID
     * @return 影响行数
     */
    @AuthAction(targetName = "/degrade/remove", value = AuthService.PrivilegeType.ALL)
    @GetMapping("/remove")
    @ResponseBody
    public Result<Integer> remove(Long id) {
        return degradeRuleService.remove(id);
    }
}
