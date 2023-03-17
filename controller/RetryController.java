package com.shopee.seamiter.controller;

import com.shopee.seamiter.auth.AuthAction;
import com.shopee.seamiter.auth.AuthService;
import com.shopee.seamiter.domain.RetryRule;
import com.shopee.seamiter.service.RetryRuleService;
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
@RequestMapping(value = "/retry")
public class RetryController {

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(RetryController.class.getSimpleName());

    /**
     * 重试服务
     */
    @Resource
    private RetryRuleService retryRuleService;

    /**
     * 获取重试规则
     *
     * @param page           页码
     * @param limit          分页大小
     * @param app            应用名称
     * @param resource       资源名称
     * @param retryPolicy    重试策略
     * @param backoffPolicy  回退策略
     * @param permissionApps 授权应用
     * @return 规则配置
     */
    @GetMapping("/rules")
    public Map<String, Object> queryRules(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) String app, @RequestParam(required = false) String resource,
            @RequestParam(required = false) Integer retryPolicy, @RequestParam(required = false) Integer backoffPolicy,
            @RequestParam(required = false) Boolean open,
            @RequestParam(required = false) List<String> permissionApps) {
        // page list
        List<RetryRule> list = retryRuleService.pageList((page - 1) * limit, limit, app, resource, retryPolicy, backoffPolicy, open, permissionApps);
        int listCount = retryRuleService.pageListCount((page - 1) * limit, limit, app, resource, retryPolicy, backoffPolicy, open, permissionApps);
        return Result.ofMap(list, listCount);
    }

    /**
     * 规则保存
     *
     * @param retryRule 重试规则
     * @return 影响行数
     */
    @AuthAction(targetName = "/retry/add", value = AuthService.PrivilegeType.ALL)
    @PostMapping("/add")
    @ResponseBody
    public Result<Integer> add(@RequestBody RetryRule retryRule) {
        logger.info("RetryController.add，param:{}", retryRule);
        return retryRuleService.save(retryRule);
    }

    /**
     * 规则加载
     *
     * @param id 规则Id
     * @return 规则
     */
    @GetMapping("/load")
    @ResponseBody
    public Result<RetryRule> load(Long id) {
        return retryRuleService.load(id);
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
    public Result<RetryRule> loadBy(String app, String resource) {
        return retryRuleService.loadBy(app, resource);
    }

    /**
     * 移除
     *
     * @param id 规则ID
     * @return 影响行数
     */
    @AuthAction(targetName = "/retry/remove", value = AuthService.PrivilegeType.ALL)
    @GetMapping("/remove")
    @ResponseBody
    public Result<?> remove(Long id) {
        return retryRuleService.remove(id);
    }

    /**
     * 规则更新
     *
     * @param retryRule 降级规则
     * @return 影响行数
     */
    @AuthAction(targetName = "/retry/update", value = AuthService.PrivilegeType.ALL)
    @PostMapping("/update")
    @ResponseBody
    public Result<Integer> update(@RequestBody RetryRule retryRule) {
        return retryRuleService.update(retryRule);
    }
}
