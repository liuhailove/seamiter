package com.shopee.seamiter.controller;

import com.shopee.seamiter.auth.AuthAction;
import com.shopee.seamiter.auth.AuthService;
import com.shopee.seamiter.domain.MockItem;
import com.shopee.seamiter.domain.MockRule;
import com.shopee.seamiter.domain.ResourceRequest;
import com.shopee.seamiter.service.MockRuleService;
import com.shopee.seamiter.service.ResourceRequestService;
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
@RequestMapping(value = "/mock")
public class MockController {

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(MockController.class.getSimpleName());

    /**
     * 流控服务
     */
    @Resource
    private MockRuleService mockRuleService;

    /**
     * 资源请求服务
     */
    @Resource
    private ResourceRequestService resourceRequestService;

    /**
     * 获取机器配置的流控规则
     *
     * @param page            页码
     * @param limit           分页大小
     * @param app             应用名称
     * @param resource        资源名称
     * @param strategy        流控模式
     * @param controlBehavior 流控效果
     * @param permissionApps  授权应用
     * @return 规则配置
     */
    @GetMapping("/rules")
    public Map<String, Object> queryMachineRules(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) String app, @RequestParam(required = false) String resource,
            @RequestParam(required = false) Integer strategy,
            @RequestParam(required = false) Integer controlBehavior,
            @RequestParam(required = false) Boolean open,
            @RequestParam(required = false) List<String> permissionApps) {
        // page list
        List<MockRule> list = mockRuleService.pageList((page - 1) * limit, limit, app, resource, strategy, controlBehavior, open, permissionApps);
        int listCount = mockRuleService.pageListCount((page - 1) * limit, limit, app, resource, strategy, controlBehavior, open, permissionApps);
        return Result.ofMap(list, listCount);
    }

    /**
     * 规则保存
     *
     * @param mockRule mock规则
     * @return 影响行数
     */
    @AuthAction(targetName = "/mock/add", value = AuthService.PrivilegeType.ALL)
    @PostMapping("/add")
    @ResponseBody
    public Result<?> add(@RequestBody MockRule mockRule) {
        logger.info("MockController.add，param:{}", mockRule);
        return mockRuleService.add(mockRule);
    }

    /**
     * 规则加载
     *
     * @param id 规则Id
     * @return 规则
     */
    @GetMapping("/load")
    @ResponseBody
    public Result<MockRule> load(Long id) {
        return Result.ofSuccess(mockRuleService.load(id));
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
    public Result<MockRule> loadBy(String app, String resource) {
        return mockRuleService.loadBy(app, resource);
    }

    /**
     * 移除
     *
     * @param id 规则ID
     * @return 影响行数
     */
    @AuthAction(targetName = "/mock/remove", value = AuthService.PrivilegeType.ALL)
    @GetMapping("/remove")
    @ResponseBody
    public Result<Integer> remove(Long id) {
        return mockRuleService.remove(id);
    }

    /**
     * 规则更新
     *
     * @param mockRule 规则
     * @return 影响行数
     */
    @AuthAction(targetName = "/mock/update", value = AuthService.PrivilegeType.ALL)
    @PostMapping("/update")
    @ResponseBody
    public Result<Integer> update(@RequestBody MockRule mockRule) {
        return mockRuleService.update(mockRule);
    }

    /**
     * 更新规则是否要保留请求
     *
     * @param id          规则ID
     * @param requestHold 请求保留标记
     * @return 成功返回1，否则返回0
     */
    @GetMapping("/updateRequestHold")
    @ResponseBody
    public Result<Integer> updateRequestHold(Long id, boolean requestHold) {
        return mockRuleService.updateRequestHold(id, requestHold);
    }

    /**
     * 获取Mock例外项
     *
     * @param page       页码
     * @param limit      分页大小
     * @param mockRuleId mock主键ID
     * @return 规则配置
     */
    @GetMapping("/items")
    public Map<String, Object> queryItems(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam Long mockRuleId) {
        // page list
        List<MockItem> list = mockRuleService.itemPageList((page - 1) * limit, limit, mockRuleId);
        int listCount = mockRuleService.itemPageListCount((page - 1) * limit, limit, mockRuleId);
        return Result.ofMap(list, listCount);
    }

    /**
     * 增加例外项
     *
     * @param mockItem 例外项
     * @return 影响行数
     */
    @PostMapping("/addItem")
    @ResponseBody
    public Result<?> addItem(@RequestBody MockItem mockItem) {
        return mockRuleService.addItem(mockItem);
    }

    /**
     * 更新例外项
     *
     * @param mockItem 例外项
     * @return 影响行数
     */
    @PostMapping("/updateItem")
    @ResponseBody
    public Result<?> updateItem(@RequestBody MockItem mockItem) {
        return mockRuleService.updateItem(mockItem);
    }

    /**
     * 加载例外项
     *
     * @param id 主键ID
     * @return 影响行数
     */
    @GetMapping("/loadItem")
    @ResponseBody
    public Result<MockItem> loadItem(Long id) {
        return Result.ofSuccess(mockRuleService.loadItem(id));
    }

    /**
     * 移除
     *
     * @param id Mock例外项ID
     * @return 影响行数
     */
    @GetMapping("/removeItem")
    @ResponseBody
    public Result<?> removeItem(Long id) {
        return mockRuleService.removeItem(id);
    }


    /**
     * 上移
     *
     * @param mockItemId item主键ID
     * @return 影响行数
     */
    @GetMapping("/moveUp")
    @ResponseBody
    public Result<Integer> moveUp(Long mockItemId) {
        return mockRuleService.moveUp(mockItemId);
    }

    /**
     * 下移
     *
     * @param mockItemId item主键ID
     * @return 影响行数
     */
    @GetMapping("/moveDown")
    @ResponseBody
    public Result<Integer> moveDown(Long mockItemId) {
        return mockRuleService.moveDown(mockItemId);
    }

    /**
     * 获取请求集合
     *
     * @param page       页码
     * @param limit      分页大小
     * @param mockRuleId mock规则ID
     * @return 请求集合
     */
    @GetMapping("/requestList")
    public Map<String, Object> requestList(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam Long mockRuleId) {
        MockRule mockRule = mockRuleService.load(mockRuleId);
        List<ResourceRequest> requestList = resourceRequestService.pageList((page - 1) * limit, limit, mockRule.getApp(), mockRule.getResource());
        int listCount = resourceRequestService.pageListCount((page - 1) * limit, limit, mockRule.getApp(), mockRule.getResource());
        return Result.ofMap(requestList, listCount);
    }


    /**
     * 获取Mock例外项
     *
     * @param mockRuleId mock主键ID
     * @return 规则配置
     */
    @GetMapping("/listAllByMock")
    public Result<List<MockItem>> listAllByMock(Long mockRuleId) {
        List<MockItem> list = mockRuleService.listAll(mockRuleId);
        return Result.ofSuccess(list);
    }


    /**
     * 更新规则是否要保留请求
     *
     * @param mockRuleId      规则ID
     * @param platformAddress 平台地址
     * @return 成功返回1，否则返回0
     */
    @GetMapping("/mockSync")
    @ResponseBody
    public Result<?> mockSync(Long mockRuleId, String platformAddress) {
        return mockRuleService.mockSync(mockRuleId, platformAddress);
    }
}
