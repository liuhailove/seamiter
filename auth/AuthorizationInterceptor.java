package com.shopee.seamiter.auth;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.shopee.seamiter.controller.RoleController;
import com.shopee.seamiter.domain.Menu;
import com.shopee.seamiter.domain.Role;
import com.shopee.seamiter.service.MenuService;
import com.shopee.seamiter.service.RoleService;
import com.shopee.seamiter.util.JwtUtils;
import com.shopee.seamiter.util.Result;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The web interceptor for privilege-based authorization.
 *
 * @author lkxiaolou
 * @author wxq
 * @since 1.7.1
 */
public class AuthorizationInterceptor implements HandlerInterceptor {
    /**
     * 菜单服务
     */
    @Resource
    private MenuService menuService;

    /**
     * 角色服务
     */
    @Resource
    private RoleService roleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            Method method = ((HandlerMethod) handler).getMethod();
            // 检查是否有token注释，有则跳过认证
            AuthAction authAction = method.getAnnotation(AuthAction.class);
            if (authAction != null) {
                String token = request.getHeader("auth0");
                Map<String, Claim> claimMap = JwtUtils.verifyToken(token);
                if (claimMap == null || claimMap.isEmpty()) {
                    responseNoPrivilegeMsg(response, authAction.message());
                    return false;
                }
                String roleName = claimMap.get("roleName").asString();
                if (RoleController.SYSTEM_MANAGER_ROLE.equals(roleName)) {
                    return true;
                }
                Role role = roleService.loadByName(roleName);
                List<Menu> menus = menuService.loadOpsBy(Arrays.asList(role.getPermissionMenus().split(",")));
                String target = request.getRequestURI();
                for (Menu menu : menus) {
                    if (target.equals(menu.getHref())) {
                        return true;
                    }
                }
                responseNoPrivilegeMsg(response, authAction.message());
                return false;
            }
        }
        return true;
    }

    private void responseNoPrivilegeMsg(HttpServletResponse response, String message) throws IOException {
        Result<?> result = Result.ofFail(-1, message);
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.getOutputStream().write(JSON.toJSONBytes(result));
    }

}
