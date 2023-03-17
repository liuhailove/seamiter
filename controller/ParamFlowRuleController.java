package com.shopee.seamiter.controller;

import com.shopee.seamiter.auth.AuthAction;
import com.shopee.seamiter.auth.AuthService;
import com.shopee.seamiter.domain.ParamFlowItem;
import com.shopee.seamiter.domain.ParamFlowRule;
import com.shopee.seamiter.service.ParamFlowRuleService;
import com.shopee.seamiter.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 参数流控规则
 *
 * @author honggang.liu
 */
@RestController
@RequestMapping(value = "/paramFlow")
public class ParamFlowRuleController {

    /**
     * 参数流控规则服务
     */
    @Resource
    private ParamFlowRuleService paramFlowRuleService;

    /**
     * 获取热点参数流控规则
     *
     * @param page           页码
     * @param limit          分页大小
     * @param app            应用名称
     * @param resource       资源名称
     * @param permissionApps 授权应用
     * @return 规则配置
     */
    @GetMapping("/rules")
    public Map<String, Object> queryRules(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) String app, @RequestParam(required = false) String resource,
            @RequestParam(required = false) Integer metricType,
            @RequestParam(required = false) List<String> permissionApps) {
        // page list
        List<ParamFlowRule> list = paramFlowRuleService.pageList((page - 1) * limit, limit, app, resource, metricType, permissionApps);
        int listCount = paramFlowRuleService.pageListCount((page - 1) * limit, limit, app, resource, metricType, permissionApps);
        return Result.ofMap(list, listCount);
    }

    /**
     * 获取热点参数例外项
     *
     * @param page        页码
     * @param limit       分页大小
     * @param paramFlowId 参数流控主键ID
     * @return 规则配置
     */
    @GetMapping("/items")
    public Map<String, Object> queryItems(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) Long paramFlowId) {
        // page list
        List<ParamFlowItem> list = paramFlowRuleService.itemPageList((page - 1) * limit, limit, paramFlowId);
        int listCount = paramFlowRuleService.itemPageListCount((page - 1) * limit, limit, paramFlowId);
        return Result.ofMap(list, listCount);
    }

    /**
     * 热点参数限流规则添加
     *
     * @param paramFlowRule 热点参数限流规则
     * @return 影响行数
     */
    @AuthAction(targetName = "/paramFlow/add", value = AuthService.PrivilegeType.ALL)
    @PostMapping("/add")
    @ResponseBody
    public Result<Integer> add(@RequestBody ParamFlowRule paramFlowRule) {
        return paramFlowRuleService.save(paramFlowRule);
    }


    /**
     * 热点参数限流规则更新
     *
     * @param paramFlowRule 热点参数限流规则
     * @return 影响行数
     */
    @AuthAction(targetName = "/paramFlow/update", value = AuthService.PrivilegeType.ALL)
    @PostMapping("/update")
    @ResponseBody
    public Result<Integer> update(@RequestBody ParamFlowRule paramFlowRule) {
        return paramFlowRuleService.update(paramFlowRule);
    }

    /**
     * 热点参数限流规则加载
     *
     * @param id 热点参数限流规则Id
     * @return 规则
     */
    @GetMapping("/load")
    @ResponseBody
    public Result<ParamFlowRule> load(Long id) {
        return Result.ofSuccess(paramFlowRuleService.load(id));
    }

    /**
     * 规则加载
     *
     * @param app      应用名称
     * @param resource 资源名称
     * @return 规则
     */
    @GetMapping("/loadBy")
    @ResponseBody
    public Result<ParamFlowRule> loadBy(String app, String resource) {
        return paramFlowRuleService.loadBy(app, resource);
    }

    /**
     * 增加例外项
     *
     * @param paramFlowItem 例外项
     * @return 影响行数
     */
    @PostMapping("/addItem")
    @ResponseBody
    public Result<?> addItem(@RequestBody ParamFlowItem paramFlowItem) {
        return paramFlowRuleService.addItem(paramFlowItem);
    }

    /**
     * 移除
     *
     * @param id 热点参数限流规则ID
     * @return 影响行数
     */
    @AuthAction(targetName = "/paramFlow/remove", value = AuthService.PrivilegeType.ALL)
    @GetMapping("/remove")
    @ResponseBody
    public Result<?> remove(Long id) {
        return paramFlowRuleService.remove(id);
    }

    /**
     * 移除
     *
     * @param id 热点参数例外项ID
     * @return 影响行数
     */
    @GetMapping("/removeItem")
    @ResponseBody
    public Result<?> removeItem(Long id) {
        return paramFlowRuleService.removeItem(id);
    }

}
