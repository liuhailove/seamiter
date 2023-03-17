package com.shopee.seamiter.controller;

import com.shopee.seamiter.config.AdminConfig;
import com.shopee.seamiter.domain.Menu;
import com.shopee.seamiter.domain.MenuInfo;
import com.shopee.seamiter.domain.Role;
import com.shopee.seamiter.domain.SystemInfo;
import com.shopee.seamiter.service.MenuService;
import com.shopee.seamiter.service.RoleService;
import com.shopee.seamiter.util.I18nUtil;
import com.shopee.seamiter.util.Result;
import com.shopee.seamiter.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * job role controller
 *
 * @author honggang.liu
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    private Logger logger = LoggerFactory.getLogger(RoleController.class.getSimpleName());

    /**
     * 角色管理
     */
    @Resource
    public RoleService roleService;

    /**
     * 菜单管理
     */
    @Resource
    private MenuService menuService;
    /**
     * 系统管理员角色
     */
    public static final String SYSTEM_MANAGER_ROLE = "SYSTEM_MANAGER_ROLE";

    @GetMapping("/pageList")
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer limit) {

        // 此处主要为了兼容新版UI
        if (page != null && limit != null) {
            length = limit;
            start = (page - 1) * limit;
        }
        // page query
        List<Role> list = roleService.pageList(start, length);
        int listCount = roleService.pageListCount(start, length);
        return Result.ofMap(list, listCount);
    }

    @PostMapping("/save")
    @ResponseBody
    public Result<Role> save(@RequestBody Role role) {
        logger.info("RoleController.save，param:{}", role);
        // valid
        if (role.getRoleName() == null || role.getRoleName().trim().length() == 0) {
            return Result.ofFail(500, (I18nUtil.getString("system_please_input") + "RoleName"));
        }
        if (role.getRoleName().length() < 4 || role.getRoleName().length() > 64) {
            return Result.ofFail(500, I18nUtil.getString("rolename_length"));
        }
        if (role.getRoleName().contains(">") || role.getRoleName().contains("<")) {
            return Result.ofFail(500, "RoleName" + I18nUtil.getString("system_unvalid"));
        }
        if (StringUtils.isContainChinese(role.getRoleName())) {
            return Result.ofFail(500, I18nUtil.getString("rolename_contain_chinese"));
        }
        role.setRoleName(role.getRoleName().trim());
        role.setAddTime(new Date());
        role.setUpdateTime(new Date());
        int ret = roleService.save(role);
        return (ret > 0) ? Result.ofSuccess(role) : Result.ofFail();
    }

    @PostMapping("/update")
    @ResponseBody
    public Result<String> update(@RequestBody Role role) {
        logger.info("RoleController.update，param:{}", role);
        // valid
        if (role.getRoleName() == null || role.getRoleName().trim().length() == 0) {
            return Result.ofFail(500, (I18nUtil.getString("system_please_input") + "RoleName"));
        }
        if (role.getRoleName().length() < 4 || role.getRoleName().length() > 64) {
            return Result.ofFail(500, I18nUtil.getString("jobconf_field_rolename_length"));
        }
        if (role.getRoleName().contains(">") || role.getRoleName().contains("<")) {
            return Result.ofFail(500, "RoleName" + I18nUtil.getString("system_unvalid"));
        }
        if (StringUtils.isContainChinese(role.getRoleName())) {
            return Result.ofFail(500, I18nUtil.getString("jobconf_field_rolename_contain_chinese"));
        }
        Role oldJobRole = roleService.loadByName(role.getRoleName());
        oldJobRole.setAuthor(role.getAuthor());
        oldJobRole.setRoleName(role.getRoleName());
        oldJobRole.setRoleDesc(role.getRoleDesc());
        oldJobRole.setPermissionUrls(role.getPermissionUrls());
        oldJobRole.setPermissionMenus(role.getPermissionMenus());
        oldJobRole.setUpdateTime(new Date());
        int ret = roleService.update(oldJobRole);
        return (ret > 0) ? Result.ofSuccess() : Result.ofFail();
    }

    @GetMapping("/remove")
    @ResponseBody
    public Result<String> remove(String roleName) {
        Role role = roleService.loadByName(roleName);
        int ret = roleService.delete(role.getId());
        return (ret > 0) ? Result.ofSuccess() : Result.ofFail();
    }

    @GetMapping("/loadById")
    @ResponseBody
    public Result<Role> loadById(int id) {
        Role role = roleService.load(id);
        return role != null ? Result.ofSuccess(role) : Result.ofFail();
    }

    @GetMapping("/loadByRoleName")
    @ResponseBody
    public Result<Role> loadByRoleName(@RequestParam String roleName) {
        Role role = roleService.loadByName(roleName);
        return role != null ? Result.ofSuccess(role) : Result.ofFail();
    }

    @GetMapping("/loadSystemMenu")
    @ResponseBody
    public SystemInfo loadSystemMenu(@RequestParam String roleName) {
        List<MenuInfo> menuInfos = new ArrayList<>();
        List<Menu> menus;
        if (!org.springframework.util.StringUtils.hasLength(roleName)) {
            menus = Collections.emptyList();
        } else if (SYSTEM_MANAGER_ROLE.equals(roleName)) {
            menus = menuService.loadsAll();
        } else {
            // 构造菜单
            Role role = roleService.loadByName(roleName);
            menus = menuService.loads(Arrays.asList(role.getPermissionMenus().split(",")));
        }
        if (!CollectionUtils.isEmpty(menus)) {
            for (Menu menu : menus) {
                MenuInfo menuInfo = new MenuInfo();
                menuInfo.setName(menu.getMenuName());
                menuInfo.setHref(menu.getHref());
                menuInfo.setTitle(menu.getTitle());
                menuInfo.setIcon(menu.getIcon());
                menuInfo.setImage(menu.getImage());
                menuInfo.setTarget(menu.getTarget());
                menuInfo.setParent(menu.getParent());
                menuInfos.add(menuInfo);
            }
            menuInfos = MenuInfo.buildTree(menuInfos);
        }
        return new SystemInfo(menuInfos);
    }

    @GetMapping("/loadOp")
    @ResponseBody
    public Result<List<Menu>> loadOp(@RequestParam String roleName, @RequestParam String parent) {
        if (SYSTEM_MANAGER_ROLE.equals(roleName)) {
            return Result.ofSuccess(menuService.loadAllOps());
        }
        Role role = roleService.loadByName(roleName);
        return Result.ofSuccess(menuService.loadOps(Arrays.asList(role.getPermissionMenus().split(",")), parent));
    }

    @GetMapping("/invalidLoadOpCache")
    @ResponseBody
    public Result<String> invalidLoadOpCache(@RequestParam String roleName, @RequestParam String parent) {
        Cache menuCache = AdminConfig.getAdminConfig().getCacheManager().getCache("menuCache");
        if (menuCache == null) {
            return Result.ofSuccess();
        }
        menuCache.evictIfPresent("loadOp-" + roleName + parent);
        return Result.ofSuccess();
    }
}
