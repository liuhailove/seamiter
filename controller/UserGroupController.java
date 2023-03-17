package com.shopee.seamiter.controller;

import com.shopee.seamiter.domain.AuthUser;
import com.shopee.seamiter.domain.UserGroup;
import com.shopee.seamiter.service.AuthUserService;
import com.shopee.seamiter.service.MachineDiscoveryService;
import com.shopee.seamiter.service.UserGroupService;
import com.shopee.seamiter.util.I18nUtil;
import com.shopee.seamiter.util.Result;
import com.shopee.seamiter.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * job role controller
 *
 * @author honggang.liu
 */
@RestController
@RequestMapping("/usergroup")
public class UserGroupController {

    private Logger logger = LoggerFactory.getLogger(UserGroupController.class.getSimpleName());

    /**
     * 组管理
     */
    @Resource
    private UserGroupService userGroupService;

    /**
     * 用户服务
     */
    @Resource
    private AuthUserService authUserService;

    /**
     * 机器注册发现服务
     */
    @Resource
    private MachineDiscoveryService machineDiscoveryService;

    @GetMapping("/pageList")
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer limit,
                                        @RequestParam(required = false) String groupName,
                                        @RequestParam(required = false) List<String> groupNames) {
        int start = 0;
        int length = 10;
        if (page != null && limit != null) {
            length = limit;
            start = (page - 1) * limit;
        }
        // page query
        List<UserGroup> list = userGroupService.pageList(start, length, groupName, groupNames);
        int listCount = userGroupService.pageListCount(start, length, groupName, groupNames);
        return Result.ofMap(list, listCount);
    }

    @PostMapping("/save")
    public Result<UserGroup> save(@RequestBody UserGroup userGroup) {
        logger.info("UserGroupController.save，param:{}", userGroup);
        // valid
        if (userGroup.getGroupName() == null || userGroup.getGroupName().trim().length() == 0) {
            return Result.ofFail(Result.FAIL_CODE, (I18nUtil.getString("system_please_input") + "GroupName"));
        }
        if (userGroup.getGroupName().length() < 4 || userGroup.getGroupName().length() > 64) {
            return Result.ofFail(Result.FAIL_CODE, I18nUtil.getString("jobugroup_field_name_length"));
        }
        if (userGroup.getGroupName().contains(">") || userGroup.getGroupName().contains("<")) {
            return Result.ofFail(Result.FAIL_CODE, "GroupName" + I18nUtil.getString("system_unvalid"));
        }
        if (StringUtils.isContainChinese(userGroup.getGroupName())) {
            return Result.ofFail(Result.FAIL_CODE, I18nUtil.getString("jobugroup_field_name_contain_chinese"));
        }
        userGroup.setGroupName(userGroup.getGroupName().trim());
        userGroup.setAddTime(new Date());
        userGroup.setUpdateTime(new Date());
        int ret = userGroupService.save(userGroup);
        return (ret > 0) ? Result.ofSuccess(userGroup) : Result.ofFail(Result.FAIL_CODE, null);
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody UserGroup userGroup) {
        logger.info("JobUserGroupController.update，param:{}", userGroup);
        // valid
        if (userGroup.getGroupName() == null || userGroup.getGroupName().trim().length() == 0) {
            return Result.ofFail(500, (I18nUtil.getString("system_please_input") + "GroupName"));
        }
        if (userGroup.getGroupName().length() < 4 || userGroup.getGroupName().length() > 64) {
            return Result.ofFail(Result.FAIL_CODE, I18nUtil.getString("jobugroup_field_name_length"));
        }
        if (userGroup.getGroupName().contains(">") || userGroup.getGroupName().contains("<")) {
            return Result.ofFail(Result.FAIL_CODE, "GroupName" + I18nUtil.getString("system_unvalid"));
        }
        if (StringUtils.isContainChinese(userGroup.getGroupName())) {
            return Result.ofFail(Result.FAIL_CODE, I18nUtil.getString("jobugroup_field_name_contain_chinese"));
        }
        UserGroup oldUserGroup = userGroupService.load(userGroup.getId());
        // 组名称不能修改
        userGroup.setGroupName(oldUserGroup.getGroupName());
        // 组所属的应用发生变化或者组所属的平台发生变化，都需要更新相关的用户
        if (!userGroup.getPermissionAppList().containsAll(oldUserGroup.getPermissionAppList()) ||
                !oldUserGroup.getPermissionAppList().containsAll(userGroup.getPermissionAppList()) ||
                !userGroup.getPermissionAppList().containsAll(oldUserGroup.getPermissionPlatformList()) ||
                !oldUserGroup.getPermissionAppList().containsAll(userGroup.getPermissionPlatformList())) {
            List<AuthUser> users = authUserService.loadByUGroupName(oldUserGroup.getGroupName());
            if (!CollectionUtils.isEmpty(users)) {
                for (AuthUser user : users) {
                    // 所属应用修改
                    List<String> permissionAppList = user.getPermissionAppList();
                    permissionAppList.removeAll(oldUserGroup.getPermissionAppList());
                    permissionAppList.addAll(userGroup.getPermissionAppList());
                    user.setPermissionApps(org.apache.commons.lang.StringUtils.join(permissionAppList, ","));

                    // 所属平台修改
                    List<String> permissionPlatforms = user.getPermissionPlatformList();
                    permissionPlatforms.removeAll(oldUserGroup.getPermissionPlatformList());
                    permissionPlatforms.addAll(userGroup.getPermissionPlatformList());
                    user.setPermissionPlatforms(org.apache.commons.lang.StringUtils.join(permissionPlatforms, ","));

                    user.setUpdateTime(new Date());
                    authUserService.update(user);
                }
            }
        }
        oldUserGroup.setAuthor(userGroup.getAuthor());
        oldUserGroup.setGroupName(userGroup.getGroupName());
        oldUserGroup.setGroupDesc(userGroup.getGroupDesc());
        oldUserGroup.setPermissionPlatforms(userGroup.getPermissionPlatforms());
        oldUserGroup.setPermissionApps(userGroup.getPermissionApps());
        oldUserGroup.setUpdateTime(new Date());
        int ret = userGroupService.update(oldUserGroup);
        return (ret > 0) ? Result.ofSuccess() : Result.ofFail();
    }

    @GetMapping("/remove")
    public Result<String> remove(int id) {
        UserGroup userGroup = userGroupService.load(id);
        List<AuthUser> authUsers = authUserService.loadByUGroupName(userGroup.getGroupName());
        if (!CollectionUtils.isEmpty(authUsers)) {
            for (AuthUser user : authUsers) {
                List<String> permissionGroupList = user.getPermissionGroupList();
                permissionGroupList.remove(userGroup.getGroupName());
                user.setPermissionGroups(org.apache.commons.lang.StringUtils.join(permissionGroupList, ","));
                user.setUpdateTime(new Date());
                authUserService.update(user);
            }
        }
        int ret = userGroupService.delete(id);
        return (ret > 0) ? Result.ofSuccess() : Result.ofFail();
    }

    @GetMapping("/loadById")
    public Result<UserGroup> loadById(int id) {
        UserGroup jobUserGroup = userGroupService.load(id);
        return jobUserGroup != null ? Result.ofSuccess(jobUserGroup) : Result.ofFail();
    }

    @GetMapping("/loadByUGroupName")
    public Result<UserGroup> loadByUGroupName(String groupName) {
        UserGroup userGroup = userGroupService.loadByUGroupName(groupName);
        return userGroup != null ? Result.ofSuccess(userGroup) : Result.ofFail();
    }

    @GetMapping("/removeByUGroupName")
    @ResponseBody
    public Result<String> removeByUGroupName(String groupName) {
        UserGroup userGroup = userGroupService.loadByUGroupName(groupName);
        if (Boolean.TRUE.equals(machineDiscoveryService.existUGroup(groupName))) {
            return Result.ofFail(Result.FAIL_CODE, I18nUtil.getString("jobguroup_remove_exist_relation_group"));
        }
        List<AuthUser> authUsers = authUserService.loadByUGroupName(userGroup.getGroupName());
        if (!CollectionUtils.isEmpty(authUsers)) {
            for (AuthUser user : authUsers) {
                List<String> permissionGroupList = user.getPermissionGroupList();
                permissionGroupList.remove(userGroup.getGroupName());
                user.setPermissionGroups(org.apache.commons.lang.StringUtils.join(permissionGroupList, ","));
                user.setUpdateTime(new Date());
                authUserService.update(user);
            }
        }
        int ret = userGroupService.delete(userGroup.getId());
        return (ret > 0) ? Result.ofSuccess() : Result.ofFail();
    }

    @GetMapping("/loadByUGroupNames")
    @ResponseBody
    public Result<List<UserGroup>> loadByNames(String[] groupNames) {
        if (groupNames == null || groupNames.length == 0) {
            return Result.ofSuccess(Collections.emptyList());
        }
        return Result.ofSuccess(userGroupService.loadByNames(Arrays.asList(groupNames)));
    }

    @GetMapping("/loadPermissionAppsByNames")
    @ResponseBody
    public Result<List<String>> loadPermissionAppsByNames(String[] groupNames) {
        if (groupNames == null || groupNames.length == 0) {
            return Result.ofSuccess(Collections.emptyList());
        }
        return Result.ofSuccess(userGroupService.loadPermissionAppsByNames(Arrays.asList(groupNames)));
    }
}
