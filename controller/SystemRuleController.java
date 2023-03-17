package com.shopee.seamiter.controller;

import com.shopee.seamiter.domain.SystemRule;
import com.shopee.seamiter.service.SystemRuleService;
import com.shopee.seamiter.util.Result;
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
@RequestMapping(value = "/system")
public class SystemRuleController {

    /**
     * 降级服务
     */
    @Resource
    private SystemRuleService systemRuleService;

    /**
     * 获取机器配置的流控规则
     *
     * @param page           页码
     * @param limit          分页大小
     * @param app            应用名称
     * @param permissionApps 授权应用
     * @return 规则配置
     */
    @GetMapping("/rules")
    public Map<String, Object> queryMachineRules(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) String app,
            @RequestParam(required = false) List<String> permissionApps) {
        // page list
        List<SystemRule> list = systemRuleService.pageList((page - 1) * limit, limit, app,permissionApps);
        int listCount = systemRuleService.pageListCount((page - 1) * limit, limit, app,permissionApps);
        return Result.ofMap(list, listCount);
    }


    /**
     * 降级规则
     *
     * @param degradeRule 降级规则
     * @return 影响行数
     */
    @PostMapping("/add")
    @ResponseBody
    public Result<Integer> add(@RequestBody SystemRule systemRule) {
        return systemRuleService.save(systemRule);
    }


    /**
     * 移除系统规则
     *
     * @param id 主键ID
     * @return 影响行数
     */
    @GetMapping("/remove")
    @ResponseBody
    public Result<Integer> remove(Long id) {
        return systemRuleService.remove(id);
    }


    /**
     * 加载系统规则
     *
     * @param id 主键ID
     * @return 系统规则
     */
    @GetMapping("/load")
    @ResponseBody
    public Result<SystemRule> load(Long id) {
        return systemRuleService.load(id);
    }


    /**
     * 规则加载
     *
     * @param app 应用名称
     * @return 规则
     */
    @GetMapping("/loadBy")
    @ResponseBody
    public Result<SystemRule> loadBy(String app) {
        return systemRuleService.loadBy(app);
    }


    /**
     * 更新系统规则
     *
     * @param systemRule 系统规则
     * @return 系统规则
     */
    @PostMapping("/update")
    @ResponseBody
    public Result<Integer> update(@RequestBody SystemRule systemRule) {
        return systemRuleService.update(systemRule);
    }

}
