package com.shopee.seamiter.controller;

import com.shopee.seamiter.domain.GrpcMethod;
import com.shopee.seamiter.domain.GrpcRequestField;
import com.shopee.seamiter.domain.GrpcResponseField;
import com.shopee.seamiter.service.GrpcDefService;
import com.shopee.seamiter.util.DateUtil;
import com.shopee.seamiter.util.FreemarkerExportUtil;
import com.shopee.seamiter.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * grpc定义controller
 *
 * @author honggang.liu
 */
@RestController
@RequestMapping(value = "/grpc")
public class GrpcDefController {

    /**
     * grpc定义服务
     */
    @Resource
    private GrpcDefService grpcDefService;

    /**
     * 分页查看
     *
     * @param page      页码
     * @param limit     分页大小
     * @param serviceId 服务ID
     * @return 分页信息
     */
    @GetMapping("/pageList")
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam Long serviceId) {
        int start = 0;
        int length = 10;
        if (page != null && limit != null) {
            length = limit;
            start = (page - 1) * limit;
        }
        // page query
        List<GrpcMethod> list = grpcDefService.pageList(start, length, serviceId);
        int listCount = grpcDefService.pageListCount(start, length, serviceId);
        return Result.ofMap(list, listCount);
    }

    /**
     * 获取服务名称
     *
     * @param page    页码
     * @param limit   分页大小
     * @param appName 应用名称
     * @return 分页信息
     */
    @GetMapping("/serviceNamePageList")
    @ResponseBody
    public Map<String, Object> serviceNamePageList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                   @RequestParam(required = false, defaultValue = "10") Integer limit,
                                                   @RequestParam String appName) {
        int start = 0;
        int length = 10;
        if (page != null && limit != null) {
            length = limit;
            start = (page - 1) * limit;
        }
        List<GrpcMethod> list = grpcDefService.serviceNamePageList(start, length, appName);
        int listCount = grpcDefService.serviceNamePageListCount(start, length, appName);
        return Result.ofMap(list, listCount);
    }

    /**
     * 获取服务名称
     *
     * @param appName 应用名称
     * @return 分页信息
     */
    @GetMapping("/queryService")
    @ResponseBody
    public Result<List<GrpcMethod>> queryService(@RequestParam String appName) {
        List<GrpcMethod> list = grpcDefService.serviceNamePageList(0, Integer.MAX_VALUE, appName);
        return Result.ofSuccess(list);
    }

    /**
     * 获取方法名称
     *
     * @param appName 应用名称
     * @return 分页信息
     */
    @GetMapping("/queryMethod")
    @ResponseBody
    public Result<List<GrpcMethod>> queryMethod(@RequestParam String appName, @RequestParam String serviceName) {
        List<GrpcMethod> list = grpcDefService.methodNamePageList(0, Integer.MAX_VALUE, appName, serviceName);
        return Result.ofSuccess(list);
    }


    /**
     * 获取方法名称
     *
     * @param appName 应用名称
     * @return 分页信息
     */
    @GetMapping("/queryMethodDetail")
    @ResponseBody
    public Result<GrpcMethod> queryMethodDetail(@RequestParam String appName, @RequestParam String serviceName, @RequestParam String methodName) {
        GrpcMethod grpcMethod = grpcDefService.queryMethodDetail(appName, serviceName, methodName);
        return Result.ofSuccess(grpcMethod);
    }


    /**
     * 获取方法明细
     *
     * @param methodId 方法ID
     * @return 分页信息
     */
    @GetMapping("/load")
    @ResponseBody
    public Result<GrpcMethod> load(@RequestParam Long methodId) {
        GrpcMethod grpcMethod = grpcDefService.load(methodId);
        return Result.ofSuccess(grpcMethod);
    }

    /**
     * 获取方法名称
     *
     * @param page        页码
     * @param limit       分页大小
     * @param appName     应用名称
     * @param serviceName 服务名称
     * @return 分页信息
     */
    @GetMapping("/methodNamePageList")
    @ResponseBody
    public Map<String, Object> methodNamePageList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(required = false, defaultValue = "10") Integer limit,
                                                  @RequestParam String appName, @RequestParam String serviceName) {
        int start = 0;
        int length = 10;
        if (page != null && limit != null) {
            length = limit;
            start = (page - 1) * limit;
        }
        List<GrpcMethod> list = grpcDefService.methodNamePageList(start, length, appName, serviceName);
        int listCount = grpcDefService.methodNamePageListCount(start, length, appName, serviceName);
        return Result.ofMap(list, listCount);
    }

    /**
     * 获取方法明细
     *
     * @param methodId 方法ID
     * @return 分页信息
     */
    @GetMapping("/listAllGrpcRequest")
    @ResponseBody
    public Result<List<GrpcRequestField>> listAllGrpcRequest(@RequestParam Long methodId) {
        return Result.ofSuccess(grpcDefService.listAllGrpcRequest(methodId));
    }

    /**
     * 更新Grpc Request 描述信息
     *
     * @param grpcRequestField 请求field
     * @return 影响行数
     */
    @PostMapping("/updateGrpcRequestDesc")
    @ResponseBody
    public Result<Integer> updateGrpcRequestDesc(@RequestBody GrpcRequestField grpcRequestField) {
        return Result.ofSuccess(grpcDefService.updateGrpcRequestDesc(grpcRequestField));
    }

    /**
     * 获取方法明细
     *
     * @param methodId 方法ID
     * @return 分页信息
     */
    @GetMapping("/listAllGrpcResponse")
    @ResponseBody
    public Result<List<GrpcResponseField>> listAllGrpcResponse(@RequestParam Long methodId) {
        return Result.ofSuccess(grpcDefService.listAllGrpcResponse(methodId));
    }

    /**
     * 更新Grpc Response 描述信息
     *
     * @param grpcResponseField 响应field
     * @return 影响行数
     */
    @PostMapping("/updateGrpcResponseDesc")
    @ResponseBody
    public Result<Integer> updateGrpcResponseDesc(@RequestBody GrpcResponseField grpcResponseField) {
        return Result.ofSuccess(grpcDefService.updateGrpcResponseDesc(grpcResponseField));
    }

    /**
     * 更新Grpc 方法描述信息
     *
     * @param grpcMethod grpc方法
     * @return 影响行数
     */
    @PostMapping("/updateMethodDesc")
    @ResponseBody
    public Result<Integer> updateMethodDesc(@RequestBody GrpcMethod grpcMethod) {
        return Result.ofSuccess(grpcDefService.updateMethodDesc(grpcMethod));
    }

    /**
     * 更新Grpc请求field
     *
     * @param grpcRequestField grpc请求Field
     * @return 影响行数
     */
    @PostMapping("/updateGrpcRequestFieldRequired")
    @ResponseBody
    public Result<Integer> updateGrpcRequestFieldRequired(@RequestBody GrpcRequestField grpcRequestField) {
        return Result.ofSuccess(grpcDefService.updateGrpcRequestFieldRequired(grpcRequestField));
    }

    /**
     * 更新grpc响应field
     *
     * @param grpcResponseField grpc响应field
     * @return 影响行数
     */
    @PostMapping("/updateGrpcResponseFieldRequired")
    @ResponseBody
    public Result<Integer> updateGrpcResponseFieldRequired(@RequestBody GrpcResponseField grpcResponseField) {
        return Result.ofSuccess(grpcDefService.updateGrpcResponseFieldRequired(grpcResponseField));
    }

    /**
     * 更新grpc响应field
     *
     * @param grpcRequestField grpc请求field
     * @return 影响行数
     */
    @PostMapping("/updateGrpcRequestFieldDbColumn")
    @ResponseBody
    public Result<Integer> updateGrpcRequestFieldDbColumn(@RequestBody GrpcRequestField grpcRequestField) {
        return Result.ofSuccess(grpcDefService.updateGrpcRequestFieldDbColumn(grpcRequestField));
    }

    /**
     * 更新grpc响应field
     *
     * @param grpcResponseField grpc响应field
     * @return 影响行数
     */
    @PostMapping("/updateGrpcResponseFieldDbColumn")
    @ResponseBody
    public Result<Integer> updateGrpcResponseFieldDbColumn(@RequestBody GrpcResponseField grpcResponseField) {
        return Result.ofSuccess(grpcDefService.updateGrpcResponseFieldDbColumn(grpcResponseField));
    }

    /**
     * 导出Grpc方法
     *
     * @param grpcMethodId 方法ID
     * @param response     响应
     * @throws IOException IO异常
     */
    @GetMapping("/exportGrpcMethodDoc/{grpcMethodId}")
    public void exportGrpcMethodDoc(@PathVariable("grpcMethodId") Long grpcMethodId, HttpServletResponse response) throws IOException {
        Map<String, Object> grpcMethodDetailMap = grpcDefService.queryGrpcMethodDetail(grpcMethodId);
        FreemarkerExportUtil.exportWord(response, grpcMethodDetailMap, grpcMethodDetailMap.get("methodName") + "-" + DateUtil.formatDateTimeSimple(new Date()) + ".doc", "interface_doc.ftl");
    }

    /**
     * 导出Grpc方法(PDF)
     *
     * @param grpcMethodId 方法ID
     * @param response     响应
     * @throws IOException IO异常
     */
    @GetMapping("/exportGrpcMethodPfd/{grpcMethodId}")
    public void exportGrpcMethodPfd(@PathVariable("grpcMethodId") Long grpcMethodId, HttpServletResponse response) throws IOException {
        Map<String, Object> grpcMethodDetailMap = grpcDefService.queryGrpcMethodDetail(grpcMethodId);
        FreemarkerExportUtil.exportPdf(response, grpcMethodDetailMap, grpcMethodDetailMap.get("methodName") + "-" + DateUtil.formatDateTimeSimple(new Date()) + ".pdf", "interface_doc.ftl");
    }

    /**
     * 导出Grpc历史
     *
     * @param grpcMethodId 方法ID
     * @param response     响应
     * @throws IOException IO异常
     */
    @GetMapping("/exportGrpcMethodHistory/{grpcMethodId}")
    public void exportGrpcMethodHistory(@PathVariable("grpcMethodId") Long grpcMethodId, HttpServletResponse response) throws IOException {
        Map<String, Object> grpcMethodDetailMap = grpcDefService.queryGrpcMethodHistory(grpcMethodId);
        FreemarkerExportUtil.exportWord(response, grpcMethodDetailMap, grpcMethodDetailMap.get("methodName") + "-" + DateUtil.formatDateTimeSimple(new Date()), "interface_history_doc.ftl");
    }

    /**
     * 导出Grpc方法(PDF)
     *
     * @param grpcMethodId 方法ID
     * @param response     响应
     * @throws IOException IO异常
     */
    @GetMapping("/exportGrpcMethodHtml/{grpcMethodId}")
    public void exportGrpcMethodHtml(@PathVariable("grpcMethodId") Long grpcMethodId, HttpServletResponse response) throws IOException {
        Map<String, Object> grpcMethodDetailMap = grpcDefService.queryGrpcMethodDetail(grpcMethodId);
        FreemarkerExportUtil.exportHtml(response, grpcMethodDetailMap, grpcMethodDetailMap.get("methodName") + "-" + DateUtil.formatDateTimeSimple(new Date()) + ".html", "html_interface_doc.ftl");
    }

    /**
     * 方法历史
     *
     * @param page         页码
     * @param limit        分页大小
     * @param grpcMethodId 方法ID
     * @return 分页信息
     */
    @GetMapping("/methodHistory")
    @ResponseBody
    public Map<String, Object> methodHistory(@RequestParam(required = false, defaultValue = "1") Integer page,
                                             @RequestParam(required = false, defaultValue = "10") Integer limit,
                                             @RequestParam Long grpcMethodId) {
        List<GrpcMethod> list = grpcDefService.queryGrpcMethodHistoryList(grpcMethodId);
        return Result.ofMap(list, list.size());
    }
}
