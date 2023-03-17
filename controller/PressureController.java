package com.shopee.seamiter.controller;

import com.shopee.seamiter.domain.PressureRule;
import com.shopee.seamiter.domain.TriggerInfo;
import com.shopee.seamiter.service.GrpcDefService;
import com.shopee.seamiter.service.PressureRuleService;
import com.shopee.seamiter.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 应用压测
 *
 * @author honggang.liu
 */
@RestController
@RequestMapping(value = "/pressure")
public class PressureController {

    /**
     * 压测服务
     */
    @Resource
    private PressureRuleService pressureRuleService;

    @Resource
    private GrpcDefService grpcDefService;

    /**
     * 压测配置列表
     *
     * @param page           偏移量
     * @param limit          分页大小
     * @param app            应用名称
     * @param sceneName      压测场景名称
     * @param serviceName    输入应用的服务
     * @param permissionApps 授权应用
     * @return 规则配置
     */
    @GetMapping("/rules")
    public Map<String, Object> queryMachineRules(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) String app, @RequestParam(required = false) String sceneName,
            @RequestParam(required = false) String serviceName,
            @RequestParam(required = false) String methodName,
            @RequestParam(required = false) List<String> permissionApps) {
        List<PressureRule> list = pressureRuleService.pageList((page - 1) * limit, limit, app, sceneName, serviceName, methodName, permissionApps);
        int listCount = pressureRuleService.pageListCount((page - 1) * limit, limit, app, sceneName, serviceName, methodName, permissionApps);
        return Result.ofMap(list, listCount);
    }

    /**
     * 规则ID
     *
     * @param id 主键ID
     * @return 压测规则
     */
    @GetMapping("/load")
    public Result<PressureRule> load(Long id) {
        return Result.ofSuccess(pressureRuleService.load(id));
    }


    /**
     * 规则保存
     *
     * @param pressureRule 规则
     * @return 影响行数
     */
    @PostMapping("/add")
    public Result<Integer> add(@RequestBody PressureRule pressureRule) {
        return Result.ofSuccess(pressureRuleService.save(pressureRule));
    }

    /**
     * 规则更新
     *
     * @param pressureTest 压测规则
     * @return 影响行数
     */
    @PostMapping("/update")
    @ResponseBody
    public Result<Integer> update(@RequestBody PressureRule pressureRule) {
        return Result.ofSuccess(pressureRuleService.update(pressureRule));
    }

    /**
     * 移除规则
     *
     * @param id 规则ID
     * @return 影响行数
     */
    @GetMapping("/remove")
    public Result<Integer> remove(Long id) {
        return Result.ofSuccess(pressureRuleService.remove(id));
    }

    /**
     * grpc调用一次
     *
     * @param triggerInfo 触发信息
     * @return 调用结果
     */
    @PostMapping("/trigger")
    public Result<Object> trigger(@RequestBody TriggerInfo triggerInfo) {
        return pressureRuleService.callOnce(triggerInfo);
    }

    /**
     * 启动压力测试
     *
     * @param id 规则ID
     * @return 成功返回true, 否则返回false
     */
    @GetMapping("/startPressure")
    public Result<Boolean> startPressure(Long id) {
        //return pressureRuleService.startPressure(id);
        return Result.ofSuccess();
    }

}
