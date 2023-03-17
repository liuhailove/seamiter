package com.shopee.seamiter.controller;

import com.shopee.seamiter.auth.AuthAction;
import com.shopee.seamiter.auth.AuthService;
import com.shopee.seamiter.domain.*;
import com.shopee.seamiter.service.GrayService;
import com.shopee.seamiter.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 灰度规则
 *
 * @author honggang.liu
 */
@RestController
@RequestMapping(value = "/gray")
public class GrayController {

    /**
     * 灰度接口服务
     */
    @Resource
    private GrayService grayService;

    /**
     * 获取机器配置的流控规则
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
            @RequestParam(required = false) String ruleName,
            @RequestParam(required = false) Integer routerStrategy,
            @RequestParam(required = false) Boolean open,
            @RequestParam(required = false) List<String> permissionApps) {
        // page list
        List<GrayRule> list = grayService.pageList((page - 1) * limit, limit, app, resource, ruleName, routerStrategy, open, permissionApps);
        int listCount = grayService.pageListCount((page - 1) * limit, limit, app, resource, ruleName, routerStrategy, open, permissionApps);
        return Result.ofMap(list, listCount);
    }

    /**
     * 规则保存
     *
     * @param grayRule 灰度规则
     * @return 影响行数
     */
    @AuthAction(targetName = "/gray/add", value = AuthService.PrivilegeType.ALL)
    @PostMapping("/add")
    @ResponseBody
    public Result<Integer> add(@RequestBody GrayRule grayRule) {
        return grayService.add(grayRule);
    }

    /**
     * 规则加载
     *
     * @param id 规则Id
     * @return 规则
     */
    @GetMapping("/load")
    @ResponseBody
    public Result<GrayRule> load(Long id) {
        return Result.ofSuccess(grayService.load(id));
    }


    /**
     * 移除
     *
     * @param id 规则ID
     * @return 影响行数
     */
    @AuthAction(targetName = "/gray/remove", value = AuthService.PrivilegeType.ALL)
    @GetMapping("/remove")
    @ResponseBody
    public Result<Integer> remove(Long id) {
        return grayService.remove(id);
    }

    /**
     * 规则更新
     *
     * @param grayRule 规则
     * @return 影响行数
     */
    @AuthAction(targetName = "/gray/update", value = AuthService.PrivilegeType.ALL)
    @PostMapping("/update")
    @ResponseBody
    public Result<Integer> update(@RequestBody GrayRule grayRule) {
        return grayService.update(grayRule);
    }

    /**
     * 获取条件列表
     *
     * @param page       页码
     * @param limit      分页大小
     * @param grayRuleId 灰度规则ID
     * @return 规则配置
     */
    @GetMapping("/queryConditions")
    public Map<String, Object> queryConditions(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam Long grayRuleId) {
        // page list
        List<GrayCondition> list = grayService.queryConditionPageList((page - 1) * limit, limit, grayRuleId);
        int listCount = grayService.queryConditionPageListCount((page - 1) * limit, limit, grayRuleId);
        return Result.ofMap(list, listCount);
    }


    /**
     * 增加灰度条件
     *
     * @param grayCondition 灰度条件
     * @return 影响行数
     */
    @PostMapping("/addCondition")
    @ResponseBody
    public Result<Integer> addCondition(@RequestBody GrayCondition grayCondition) {
        return grayService.addCondition(grayCondition);
    }

    /**
     * 更新条件
     *
     * @param grayCondition 灰度条件
     * @return 影响行数
     */
    @PostMapping("/updateCondition")
    @ResponseBody
    public Result<Integer> updateCondition(@RequestBody GrayCondition grayCondition) {
        return grayService.updateCondition(grayCondition);
    }

    /**
     * 加载例外项
     *
     * @param id 主键ID
     * @return 影响行数
     */
    @GetMapping("/loadCondition")
    @ResponseBody
    public Result<GrayCondition> loadCondition(Long id) {
        return Result.ofSuccess(grayService.loadCondition(id));
    }

    /**
     * 移除
     *
     * @param id 条件ID
     * @return 影响行数
     */
    @GetMapping("/removeCondition")
    @ResponseBody
    public Result<Integer> removeCondition(Long id) {
        return grayService.removeCondition(id);
    }


    /**
     * 上移
     *
     * @param grayConditionId condition主键ID
     * @return 影响行数
     */
    @GetMapping("/moveUp")
    @ResponseBody
    public Result<Integer> moveUp(Long grayConditionId) {
        return grayService.moveUp(grayConditionId);
    }

    /**
     * 下移
     *
     * @param grayConditionId condition主键ID
     * @return 影响行数
     */
    @GetMapping("/moveDown")
    @ResponseBody
    public Result<Integer> moveDown(Long grayConditionId) {
        return grayService.moveDown(grayConditionId);
    }


    /**
     * 查询灰度参数
     *
     * @param grayRuleId      灰度规则
     * @param grayConditionId conditionId
     * @return 灰度条件列表
     */
    @GetMapping("/queryAllParam")
    @ResponseBody
    public Map<String, Object> queryAllParam(Long grayRuleId, Long grayConditionId) {
        List<GrayParam> list = grayService.queryAllParam(grayRuleId, grayConditionId);
        int listCount = list.size();
        return Result.ofMap(list, listCount);
    }

    /**
     * 更新灰度条件
     *
     * @param grayParam 灰度参数
     * @return 影响行数
     */
    @PostMapping("/addParam")
    @ResponseBody
    public Result<Integer> addParam(@RequestBody GrayParam grayParam) {
        return grayService.addParam(grayParam);
    }


    /**
     * 更新灰度参数
     *
     * @param grayParam 灰度参数
     * @return 影响行数
     */
    @PostMapping("/updateParam")
    @ResponseBody
    public Result<Integer> updateParam(@RequestBody GrayParam grayParam) {
        return grayService.updateParam(grayParam);
    }

    /**
     * 灰度参数加载
     *
     * @param id 主键ID
     * @return 影响行数
     */
    @GetMapping("/loadParam")
    @ResponseBody
    public Result<GrayParam> loadParam(Long id) {
        return Result.ofSuccess(grayService.loadParam(id));
    }

    /**
     * 更新灰度参数
     *
     * @param grayParamId 灰度参数ID
     * @return 影响行数
     */
    @GetMapping("/removeParam")
    @ResponseBody
    public Result<Integer> removeParam(Long grayParamId) {
        return grayService.removeParam(grayParamId);
    }

    /**
     * 获取标签列表
     *
     * @param page       页码
     * @param limit      分页大小
     * @param grayRuleId 灰度规则ID
     * @return 规则配置
     */
    @GetMapping("/queryTags")
    public Map<String, Object> queryTags(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam Long grayRuleId) {
        // page list
        List<GrayTag> list = grayService.queryTagPageList((page - 1) * limit, limit, grayRuleId);
        int listCount = grayService.queryTagPageListCount((page - 1) * limit, limit, grayRuleId);
        return Result.ofMap(list, listCount);
    }

    /**
     * 增加标签
     *
     * @param grayTag 灰度标签
     * @return 影响行数
     */
    @PostMapping("/addTag")
    @ResponseBody
    public Result<Integer> addTag(@RequestBody GrayTag grayTag) {
        return grayService.addTag(grayTag);
    }

    /**
     * 更新标签
     *
     * @param grayTag 灰度标签
     * @return 影响行数
     */
    @PostMapping("/updateTag")
    @ResponseBody
    public Result<Integer> updateTag(@RequestBody GrayTag grayTag) {
        return grayService.updateTag(grayTag);
    }

    /**
     * 加载标签
     *
     * @param id 主键ID
     * @return 影响行数
     */
    @GetMapping("/loadTag")
    @ResponseBody
    public Result<GrayTag> loadTag(Long id) {
        return Result.ofSuccess(grayService.loadTag(id));
    }

    /**
     * 移除
     *
     * @param id 条件ID
     * @return 影响行数
     */
    @GetMapping("/removeTag")
    @ResponseBody
    public Result<Integer> removeTag(Long id) {
        return grayService.removeTag(id);
    }


    /**
     * 获取标签列表
     *
     * @param page       页码
     * @param limit      分页大小
     * @param grayRuleId 灰度规则ID
     * @return 规则配置
     */
    @GetMapping("/queryWeights")
    public Map<String, Object> queryWeights(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam Long grayRuleId) {
        // page list
        List<GrayWeight> list = grayService.queryWeightPageList((page - 1) * limit, limit, grayRuleId);
        int listCount = grayService.queryWeightPageListCount((page - 1) * limit, limit, grayRuleId);
        return Result.ofMap(list, listCount);
    }

    /**
     * 增加权重
     *
     * @param grayWeight 灰度权重
     * @return 影响行数
     */
    @PostMapping("/addWeight")
    @ResponseBody
    public Result<Integer> addWeight(@RequestBody GrayWeight grayWeight) {
        return grayService.addWeight(grayWeight);
    }

    /**
     * 更新权重
     *
     * @param grayWeight 灰度权重
     * @return 影响行数
     */
    @PostMapping("/updateWeight")
    @ResponseBody
    public Result<Integer> updateWeight(@RequestBody GrayWeight grayWeight) {
        return grayService.updateWeight(grayWeight);
    }

    /**
     * 加载权重
     *
     * @param id 主键ID
     * @return 影响行数
     */
    @GetMapping("/loadWeight")
    @ResponseBody
    public Result<GrayWeight> loadWeight(Long id) {
        return Result.ofSuccess(grayService.loadWeight(id));
    }

    /**
     * 移除
     *
     * @param id 条件ID
     * @return 影响行数
     */
    @GetMapping("/removeWeight")
    @ResponseBody
    public Result<Integer> removeWeight(Long id) {
        return grayService.removeWeight(id);
    }

}
