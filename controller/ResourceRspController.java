package com.shopee.seamiter.controller;

import com.shopee.seamiter.domain.ResourceRsp;
import com.shopee.seamiter.service.ResourceRspService;
import com.shopee.seamiter.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 资源Rsp Controller
 *
 * @author honggang.liu
 */
@RestController
@RequestMapping("/rsp")
public class ResourceRspController {

    @Resource
    private ResourceRspService resourceRspService;

    /**
     * 加载资源rsp
     *
     * @param app      应用名称
     * @param resource 资源名称
     * @return 降级规则
     */
    @GetMapping("/loadBy")
    @ResponseBody
    public Result<ResourceRsp> loadBy(String app, String resource) {
        return resourceRspService.loadBy(app, resource);
    }

    /**
     * 资源集合
     *
     * @param page           分页
     * @param limit          分页大小
     * @param app            app
     * @param resource       资源名称
     * @param permissionApps 授权应用
     * @return 资源集合
     */
    @GetMapping("resources")
    @ResponseBody
    public Map<String, Object> resources(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) String app, @RequestParam(required = false) String resource,
            @RequestParam(required = false) List<String> permissionApps) {
        // page list
        List<ResourceRsp> list = resourceRspService.pageList((page - 1) * limit, limit, app, resource,permissionApps);
        int listCount = resourceRspService.pageListCount((page - 1) * limit, limit, app, resource,permissionApps);
        return Result.ofMap(list, listCount);
    }
}
