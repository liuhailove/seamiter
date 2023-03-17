package com.shopee.seamiter.controller;

import com.shopee.seamiter.domain.RealResourceMetric;
import com.shopee.seamiter.service.ResourceMetricService;
import com.shopee.seamiter.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 资源管理controller
 *
 * @author honggang.liu
 */
@RestController
@RequestMapping(value = "/resource")
public class ResourceController {

    /**
     * 流控资源服务
     */
    @Resource
    private ResourceMetricService resourceMetricService;

    /**
     * Fetch statistics info of the machine.
     *
     * @param resource       资源
     * @param app            应用
     * @param permissionApps 授权应用
     * @return node statistics info.
     */
    @GetMapping("/machineResource")
    public Map<String, Object> machineResource(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false,defaultValue = "") String app, @RequestParam(required = false,defaultValue = "") String resource,
            @RequestParam(required = false) List<String> permissionApps) {
        // page list
        List<RealResourceMetric> list = resourceMetricService.pageList((page - 1) * limit, limit, app, resource, permissionApps);
        int listCount = resourceMetricService.pageListCount((page - 1) * limit, limit, app, resource, permissionApps);
        return Result.ofMap(list, listCount);
    }
}
