package com.shopee.seamiter.controller;

import com.shopee.seamiter.domain.AlarmItem;
import com.shopee.seamiter.domain.AlarmRule;
import com.shopee.seamiter.service.AlarmRuleService;
import com.shopee.seamiter.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 告警规则Controller
 *
 * @author honggang.liu
 */
@RestController
@RequestMapping("/alarm")
public class AlarmRuleController {

    /**
     * 告警服务
     */
    @Resource
    private AlarmRuleService alarmRuleService;

    /**
     * 规则保存
     *
     * @param alarmRule 告警规则
     * @return 影响行数
     */
    @PostMapping("/add")
    public Result<Integer> add(@RequestBody AlarmRule alarmRule) {
        return alarmRuleService.add(alarmRule);
    }

    /**
     * 规则更新
     *
     * @param alarmRule 告警规则
     * @return 影响行数
     */
    @PostMapping("/update")
    @ResponseBody
    public Result<Integer> update(@RequestBody AlarmRule alarmRule) {
        return alarmRuleService.update(alarmRule);
    }

    /**
     * 移除规则
     *
     * @param id 规则ID
     * @return 影响行数
     */
    @GetMapping("/remove")
    public Result<Integer> remove(Long id) {
        return alarmRuleService.remove(id);
    }

    /**
     * 移除规则
     *
     * @param id 规则ID
     * @return 影响行数
     */
    @GetMapping("/removeItem")
    public Result<Integer> removeItem(Long id) {
        return alarmRuleService.removeItem(id);
    }

    /**
     * 分页查找
     *
     * @param page           偏移量
     * @param limit          分页大小
     * @param app            应用名称
     * @param alarmType      告警类别
     * @param alarmName      告警名称
     * @param alarmLevel     告警级别
     * @param permissionApps 授权应用
     * @return 记录数
     */
    @GetMapping("/rules")
    public Map<String, Object> queryRules(@RequestParam(required = false, defaultValue = "1") Integer page,
                                          @RequestParam(required = false, defaultValue = "10") Integer limit,
                                          @RequestParam(required = false) String app,
                                          @RequestParam(required = false) Integer resourceType,
                                          @RequestParam(required = false) Integer alarmLevel,
                                          @RequestParam(required = false) String alarmName,
                                          @RequestParam(required = false) List<String> permissionApps) {
        List<AlarmRule> list = alarmRuleService.pageList((page - 1) * limit, limit, app, resourceType, alarmLevel, alarmName, permissionApps);
        int listCount = alarmRuleService.pageListCount((page - 1) * limit, limit, app, resourceType, alarmLevel, alarmName, permissionApps);
        return Result.ofMap(list, listCount);
    }

    /**
     * 分页查找Itms
     *
     * @param page        偏移量
     * @param limit       分页大小
     * @param alarmRuleId 告警规则ID
     * @return 记录数
     */
    @GetMapping("/items")
    public Map<String, Object> queryItems(@RequestParam(required = false, defaultValue = "1") Integer page,
                                          @RequestParam(required = false, defaultValue = "10") Integer limit,
                                          @RequestParam(required = false) Long alarmRuleId) {
        List<AlarmItem> list = alarmRuleService.itemsPageList((page - 1) * limit, limit, alarmRuleId);
        int listCount = alarmRuleService.itemsPageListCount((page - 1) * limit, limit, alarmRuleId);
        return Result.ofMap(list, listCount);
    }


    /**
     * 规则ID
     *
     * @param id 主键ID
     * @return 告警规则
     */
    @GetMapping("/load")
    public Result<AlarmRule> load(Long id) {
        return Result.ofSuccess(alarmRuleService.load(id));
    }


    /**
     * 规则保存
     *
     * @param alarmItem 告警规则明细
     * @return 影响行数
     */
    @PostMapping("/addItem")
    public Result<Integer> addItem(@RequestBody AlarmItem alarmItem) {
        return alarmRuleService.addItem(alarmItem);
    }

    /**
     * 规则更新
     *
     * @param alarmItem 告警规则明细
     * @return 影响行数
     */
    @PostMapping("/updateItem")
    @ResponseBody
    public Result<Integer> updateItem(@RequestBody AlarmItem alarmItem) {
        return alarmRuleService.updateItem(alarmItem);
    }


    /**
     * 规则项ID
     *
     * @param id 主键ID
     * @return 告警规则
     */
    @GetMapping("/loadItem")
    public Result<AlarmItem> loadItem(Long id) {
        return Result.ofSuccess(alarmRuleService.loadItem(id));
    }


    /**
     * 创建关联
     *
     * @param ruleId       规则ID
     * @param resourceIdes 资源集合
     * @return 告警规则
     */
    @GetMapping("/createRelation")
    public Result<Integer> createRelation(@RequestParam("ruleId") Long ruleId, @RequestParam("resourceIdes") Long[] resourceIdes) {
        return alarmRuleService.createRelation(ruleId, resourceIdes);
    }

}
