package com.shopee.seamiter.controller;

import com.shopee.seamiter.domain.Menu;
import com.shopee.seamiter.service.MenuService;
import com.shopee.seamiter.service.RoleService;
import com.shopee.seamiter.util.I18nUtil;
import com.shopee.seamiter.util.Result;
import com.shopee.seamiter.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * job role controller
 *
 * @author honggang.liu
 */
@Controller
@RequestMapping("/menu")
public class MenuController {

    private Logger logger = LoggerFactory.getLogger(MenuController.class.getSimpleName());

    /**
     * 角色管理
     */
    @Resource
    public MenuService menuService;

    /**
     * 角色服务
     */
    @Resource
    private RoleService roleService;

    @GetMapping("/pageList")
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer limit) {
        int start = 0;
        int length = 10;
        if (page != null && limit != null) {
            length = limit;
            start = (page - 1) * limit;
        }
        // page query
        List<Menu> list = menuService.findPage(start, length);
        int listCount = menuService.findAllCount();
        return Result.ofMap(list, listCount);
    }

    @PostMapping("/save")
    @ResponseBody
    public Result<Menu> save(@RequestBody Menu menu) {
        logger.info("MenuController.save，param:{}", menu);
        // valid
        if (menu.getMenuName() == null || menu.getMenuName().trim().length() == 0) {
            return Result.ofFail(500, (I18nUtil.getString("system_please_input") + "Name"));
        }
        if (menu.getMenuName().length() < 4 || menu.getMenuName().length() > 64) {
            return Result.ofFail(500, I18nUtil.getString("urlname_length"));
        }
        if (menu.getMenuName().contains(">") || menu.getMenuName().contains("<")) {
            return Result.ofFail(500, "Name" + I18nUtil.getString("system_unvalid"));
        }
        if (StringUtils.isContainChinese(menu.getMenuName())) {
            return Result.ofFail(500, I18nUtil.getString("urlname_contain_chinese"));
        }
        int ret = menuService.save(menu);
        return (ret > 0) ? Result.ofSuccess(menu) : Result.ofFail();
    }

    @PostMapping("/update")
    @ResponseBody
    public Result<String> update(@RequestBody Menu menu) {
        logger.info("JobUrlController.update，param:{}", menu);
        // valid
        if (menu.getMenuName() == null || menu.getMenuName().trim().length() == 0) {
            return Result.ofFail(500, (I18nUtil.getString("system_please_input") + "Name"));
        }
        if (menu.getMenuName().length() < 4 || menu.getMenuName().length() > 64) {
            return Result.ofFail(500, I18nUtil.getString("jobconf_field_urlname_length"));
        }
        if (menu.getMenuName().contains(">") || menu.getMenuName().contains("<")) {
            return Result.ofFail(500, "Name" + I18nUtil.getString("system_unvalid"));
        }
        if (StringUtils.isContainChinese(menu.getMenuName())) {
            return Result.ofFail(500, I18nUtil.getString("jobconf_field_urlname_contain_chinese"));
        }
        Menu oldJobUrl = menuService.loadByName(menu.getMenuName());
        oldJobUrl.setHref(menu.getHref());
        oldJobUrl.setMenuName(menu.getMenuName());
        oldJobUrl.setTitle(menu.getTitle());
        int ret = menuService.update(oldJobUrl);
        return (ret > 0) ?  Result.ofSuccess() : Result.ofFail();
    }

    @GetMapping("/removeByName")
    @ResponseBody
    public Result<String> removeByName(String name) {
        Menu menu = menuService.loadByName(name);
        // 判断角色中包含此name的选项
        if (Boolean.TRUE.equals(roleService.existByMenu(name))) {
            return Result.ofFail(500, I18nUtil.getString("jobmenu_remove_exist_relation_role"));
        }
        // 删除用户中包含此name的选项
        int ret = menuService.delete(menu.getId());
        return (ret > 0) ? Result.ofSuccess() : Result.ofFail();
    }

    @GetMapping("/loadById")
    @ResponseBody
    public Result<Menu> loadById(int id) {
        Menu jobUrl = menuService.load(id);
        return jobUrl != null ?  Result.ofSuccess(jobUrl) : Result.ofFail();
    }

    @GetMapping("/loadByName")
    @ResponseBody
    public Result<Menu> loadByName(String name) {
        Menu jobMenu = menuService.loadByName(name);
        return jobMenu != null ? Result.ofSuccess(jobMenu) : Result.ofFail();
    }

}
