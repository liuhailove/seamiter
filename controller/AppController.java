package com.shopee.seamiter.controller;

import com.shopee.seamiter.auth.AuthAction;
import com.shopee.seamiter.auth.AuthService;
import com.shopee.seamiter.domain.AppInfo;
import com.shopee.seamiter.domain.AppInfoView;
import com.shopee.seamiter.domain.AuthUser;
import com.shopee.seamiter.domain.MachineInfo;
import com.shopee.seamiter.service.AuthUserService;
import com.shopee.seamiter.service.MachineDiscoveryService;
import com.shopee.seamiter.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.shopee.seamiter.auth.DefaultLoginAuthenticationFilter.WEB_SESSION_KEY;

/**
 * 应用管理
 *
 * @author honggang.liu
 */
@RestController
@RequestMapping("/app")
public class AppController {

    /**
     * 注册发现
     */
    @Resource
    private MachineDiscoveryService machineDiscoveryService;

    /**
     * 用户服务
     */
    @Resource
    private AuthUserService authUserService;

    /**
     * 查询应用信息
     *
     * @return 查询应用信息
     */
    @GetMapping("/briefinfos")
    @ResponseBody
    public Result<List<AppInfo>> queryAppInfos(@RequestParam(required = false, defaultValue = "1") Integer page,
                                               @RequestParam(required = false, defaultValue = "10") Integer limit,
                                               @RequestParam(required = false) String app,
                                               @RequestParam(required = false) Integer appType,
                                               @RequestParam(required = false) Boolean onlineStatus,
                                               @RequestParam(required = false) String remark) {
        return Result.ofSuccess(machineDiscoveryService.getBriefApps((page - 1) * limit, limit, app, appType, onlineStatus, remark));
    }

    /**
     * 查询应用信息
     *
     * @return 查询应用信息
     */
    @GetMapping("/queryBriefAll")
    @ResponseBody
    public Result<List<AppInfo>> queryBriefAll() {
        return Result.ofSuccess(machineDiscoveryService.getBriefApps(0, Integer.MAX_VALUE, "", null, null, null));
    }


    /**
     * 查询应用的注册机器信息
     *
     * @param page  分页
     * @param limit 分页大小
     * @param app   app明瑛
     * @return 机器信息
     */
    @GetMapping("/machine")
    public Result<List<MachineInfo>> getMachinesByApp(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                      @RequestParam(required = false, defaultValue = "10") Integer limit,
                                                      @RequestParam(required = false) String app,
                                                      @RequestParam(required = false) String ip) {
        List<MachineInfo> machineInfoList = machineDiscoveryService.getMachineDetailByApp((page - 1) * limit, limit, app, ip);
        return Result.ofSuccess(machineInfoList);
    }

    /**
     * 应用信息
     *
     * @param appInfo 应用信息
     * @return 影响行数
     */
    @AuthAction(targetName = "/app/add", value = AuthService.PrivilegeType.ALL)
    @PostMapping("/add")
    @ResponseBody
    public Result<Integer> add(HttpServletRequest request, @RequestBody AppInfo appInfo) {
        HttpSession session = request.getSession();
        Object seaUserObj = session.getAttribute(WEB_SESSION_KEY);
        AuthUser authUser = null;
        if (seaUserObj instanceof AuthUser) {
            authUser = (AuthUser) seaUserObj;
        }
        if (authUser == null) {
            return Result.ofFail(-1, "Not logged in");
        }
        authUser = authUserService.load(authUser.getId());
        if (authUser.getPermissionApps() == null) {
            authUser.setPermissionApps("");
        }
        if (authUser.getPermissionGroups() == null) {
            authUser.setPermissionGroups("");
        }
        if (authUser.getPermissionPlatforms() == null) {
            authUser.setPermissionPlatforms("");
        }
        return machineDiscoveryService.addApp(authUser,appInfo);
    }

    /**
     * 应用信息
     *
     * @param id 应用ID
     * @return 影响行数
     */
    @GetMapping("/load")
    @ResponseBody
    public Result<AppInfo> load(Long id) {
        return Result.ofSuccess(machineDiscoveryService.load(id));
    }

    /**
     * 应用信息
     *
     * @param appName 应用ID
     * @return 影响行数
     */
    @GetMapping("/getDetailApp")
    @ResponseBody
    public Result<AppInfoView> getDetailApp(String appName) {
        return Result.ofSuccess(machineDiscoveryService.getDetailApp(appName));
    }

    /**
     * 应用信息
     *
     * @param appInfo 应用
     * @return 影响行数
     */
    @AuthAction(targetName = "/app/update", value = AuthService.PrivilegeType.ALL)
    @PostMapping("/update")
    @ResponseBody
    public Result<Integer> update(@RequestBody AppInfo appInfo) {
        return machineDiscoveryService.updateApp(appInfo);
    }

    /**
     * 移除应用
     *
     * @param id 主键ID
     * @return 影响行数
     */
    @AuthAction(targetName = "/app/remove", value = AuthService.PrivilegeType.ALL)
    @GetMapping("/remove")
    @ResponseBody
    public Result<Integer> remove(Long id) {
        return machineDiscoveryService.remove(id);
    }
}
