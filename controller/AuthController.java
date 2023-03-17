/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shopee.seamiter.controller;

import com.shopee.seamiter.domain.AuthUser;
import com.shopee.seamiter.service.AuthUserService;
import com.shopee.seamiter.util.JwtUtils;
import com.shopee.seamiter.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.shopee.seamiter.auth.DefaultLoginAuthenticationFilter.WEB_SESSION_KEY;


@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class.getSimpleName());

    /**
     * 用户服务
     */
    @Resource
    private AuthUserService authUserService;

    @PostMapping("/login")
    @ResponseBody
    public Result<AuthUser> login(HttpServletRequest request, @RequestBody AuthUser authUser) {
        if (org.apache.commons.lang.StringUtils.isEmpty(authUser.getUsername())) {
            LOGGER.error("Login failed: Invalid username or password, username={}", authUser.getUsername());
            // 设置cookie为空
            return Result.ofFail(-1, "Invalid username or password");
        }
        AuthUser authUserDb = authUserService.loadByUserName(authUser.getUsername());
        if (authUserDb == null) {
            return Result.ofFail(-1, "Invalid username or password");
        }
        if (!authUserDb.getPwd().equals(DigestUtils.md5DigestAsHex(authUser.getPwd().getBytes()))) {
            return Result.ofFail(-1, "Invalid username or password");
        }
        // do login
        authUserDb.setAuthToken(JwtUtils.createToken(authUserDb));
        request.getSession().setAttribute(WEB_SESSION_KEY, authUserDb);
        return Result.ofSuccess(authUserDb);
    }

    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login.html";
    }

    @PostMapping(value = "/check")
    @ResponseBody
    public Result<?> check(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object seaUserObj = session.getAttribute(WEB_SESSION_KEY);
        AuthUser authUser = null;
        if (seaUserObj instanceof AuthUser) {
            authUser = (AuthUser) seaUserObj;
        }
        if (authUser == null) {
            return Result.ofFail(-1, "Not logged in");
        }
        AuthUser authUserDB = authUserService.load(authUser.getId());
        if (authUserDB.getPermissionApps() == null) {
            authUserDB.setPermissionApps("");
        }
        if (authUserDB.getPermissionGroups() == null) {
            authUserDB.setPermissionGroups("");
        }
        if (authUserDB.getPermissionPlatforms() == null) {
            authUserDB.setPermissionPlatforms("");
        }
        authUserDB.setAuthToken(authUser.getAuthToken());
        return Result.ofSuccess(authUserDB);
    }
}
