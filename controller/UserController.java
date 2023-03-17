package com.shopee.seamiter.controller;

import com.shopee.seamiter.domain.AuthUser;
import com.shopee.seamiter.domain.UserGroup;
import com.shopee.seamiter.service.AuthUserService;
import com.shopee.seamiter.service.UserGroupService;
import com.shopee.seamiter.util.I18nUtil;
import com.shopee.seamiter.util.Result;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.shopee.seamiter.auth.DefaultLoginAuthenticationFilter.WEB_SESSION_KEY;

/**
 * job role controller
 *
 * @author honggang.liu
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 用户服务
     */
    @Resource
    private AuthUserService authUserService;

    /**
     * 用户组服务
     */
    @Resource
    private UserGroupService userGroupService;

    @RequestMapping("/pageList")
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer limit,
                                        @RequestParam(required = false) String username,
                                        @RequestParam(required = false) String roleName) {
        int start = 0;
        int length = 10;
        if (page != null && limit != null) {
            length = limit;
            start = (page - 1) * limit;
        }
        // page list
        List<AuthUser> list = authUserService.pageList(start, length, username, roleName);
        int listCount = authUserService.pageListCount(start, length, username, roleName);
        return Result.ofMap(list, listCount);
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody AuthUser authUser) {
        // valid username
        if (!StringUtils.hasText(authUser.getUsername())) {
            return Result.ofFail(Result.FAIL_CODE, I18nUtil.getString("system_please_input") + I18nUtil.getString("user_username"));
        }
        authUser.setUsername(authUser.getUsername().trim());
        if (!(authUser.getUsername().length() >= 4 && authUser.getUsername().length() <= 60)) {
            return Result.ofFail(Result.FAIL_CODE, I18nUtil.getString("system_lengh_limit") + "[4-20]");
        }
        // valid password
        if (StringUtils.hasText(authUser.getPwd())) {
            authUser.setPwd(authUser.getPwd().trim());
            if (!(authUser.getPwd().length() >= 4 && authUser.getPwd().length() <= 60)) {
                return Result.ofFail(Result.FAIL_CODE, I18nUtil.getString("system_lengh_limit") + "[4-20]");
            }
            // md5 password
            authUser.setPwd(DigestUtils.md5DigestAsHex(authUser.getPwd().getBytes()));
        }
        // check repeat
        AuthUser existUser = authUserService.loadByUserName(authUser.getUsername());
        if (existUser != null) {
            return Result.ofFail(Result.FAIL_CODE, I18nUtil.getString("user_username_repeat"));
        }
        if (!StringUtils.isEmpty(authUser.getPermissionGroups())) {
            List<UserGroup> userGroups = userGroupService.loadByNames(Arrays.asList(authUser.getPermissionGroups().split(",")));
            Set<String> permissionPlatformSet = new LinkedHashSet<>();
            Set<String> appSet = new LinkedHashSet<>();
            if (!CollectionUtils.isEmpty(userGroups)) {
                for (UserGroup userGroup : userGroups) {
                    if (!StringUtils.isEmpty(userGroup.getPermissionPlatforms())) {
                        permissionPlatformSet.addAll(Arrays.asList(userGroup.getPermissionPlatforms().split(",")));
                    }
                    if (!StringUtils.isEmpty(userGroup.getPermissionApps())) {
                        appSet.addAll(Arrays.asList(userGroup.getPermissionApps().split(",")));
                    }
                }
            }
            authUser.setPermissionPlatforms(org.apache.commons.lang.StringUtils.join(permissionPlatformSet, ","));
            authUser.setPermissionApps(org.apache.commons.lang.StringUtils.join(appSet, ","));
        }
        authUser.setAddTime(new Date());
        authUser.setUpdateTime(new Date());
        // write
        authUserService.save(authUser);
        return Result.ofSuccess();
    }

    @PostMapping("/update")
    public Result<String> update(HttpServletRequest request, @RequestBody AuthUser authUser) {
        // valid username
        if (!StringUtils.hasText(authUser.getUsername())) {
            return Result.ofFail(Result.FAIL_CODE, I18nUtil.getString("system_please_input") + I18nUtil.getString("user_username"));
        }
        authUser.setUsername(authUser.getUsername().trim());
        if (!(authUser.getUsername().length() >= 4 && authUser.getUsername().length() <= 60)) {
            return Result.ofFail(Result.FAIL_CODE, I18nUtil.getString("system_lengh_limit") + "[4-20]");
        }
        if (!StringUtils.isEmpty(authUser.getPermissionGroups())) {
            List<UserGroup> userGroups = userGroupService.loadByNames(Arrays.asList(authUser.getPermissionGroups().split(",")));
            Set<String> permissionPlatformSet = new LinkedHashSet<>();
            Set<String> appSet = new LinkedHashSet<>();
            if (!CollectionUtils.isEmpty(userGroups)) {
                for (UserGroup userGroup : userGroups) {
                    if (!StringUtils.isEmpty(userGroup.getPermissionPlatforms())) {
                        permissionPlatformSet.addAll(Arrays.asList(userGroup.getPermissionPlatforms().split(",")));
                    }
                    if (!StringUtils.isEmpty(userGroup.getPermissionApps())) {
                        appSet.addAll(Arrays.asList(userGroup.getPermissionApps().split(",")));
                    }
                }
            }
            authUser.setPermissionPlatforms(org.apache.commons.lang.StringUtils.join(permissionPlatformSet, ","));
            authUser.setPermissionApps(org.apache.commons.lang.StringUtils.join(appSet, ","));
        }
        authUser.setAddTime(new Date());
        authUser.setUpdateTime(new Date());
        AuthUser oldUser = authUserService.loadByUserName(authUser.getUsername());
        authUser.setId(oldUser.getId());
        // write
        authUserService.update(authUser);
        // 更新session
        request.getSession().setAttribute(WEB_SESSION_KEY, authUser);
        return Result.ofSuccess();
    }

    @GetMapping("/loadByUserName")
    public Result<AuthUser> loadByUserName(String userName) {
        return Result.ofSuccess(authUserService.loadByUserName(userName));
    }

    @GetMapping("/remove")
    public Result<String> remove(String userName) {
        AuthUser user = authUserService.loadByUserName(userName);
        authUserService.delete(user.getId());
        return Result.ofSuccess();
    }

    @PostMapping("/updatePwd/{password}")
    public Result<String> updatePwd(HttpServletRequest request, @PathVariable String password) {
        // valid password
        if (password == null || password.trim().length() == 0) {
            return Result.ofFail(Result.FAIL_CODE, "密码不可为空");
        }
        password = password.trim();
        if (!(password.length() >= 4 && password.length() <= 60)) {
            return Result.ofFail(Result.FAIL_CODE, I18nUtil.getString("system_lengh_limit") + "[4-20]");
        }
        // md5 password
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        AuthUser authUser = (AuthUser) request.getSession().getAttribute(WEB_SESSION_KEY);

        // do write
        AuthUser existUser = authUserService.loadByUserName(authUser.getUsername());
        existUser.setPwd(md5Password);
        existUser.setUpdateTime(new Date());
        authUserService.updatePwd(existUser);
        return Result.ofSuccess();
    }


}
