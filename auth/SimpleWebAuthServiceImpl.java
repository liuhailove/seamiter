///*
// * Copyright 1999-2018 Alibaba Group Holding Ltd.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.shopee.sealimit.auth;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
///**
// * @author honggang.liu
// * @since 1.6.0
// */
//public class SimpleWebAuthServiceImpl implements AuthService<HttpServletRequest> {
//
//    public static final String WEB_SESSION_KEY = "session_sea_limiter_admin";
//
//    @Override
//    public AuthUser getAuthUser(HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        Object seaUserObj = session.getAttribute(SimpleWebAuthServiceImpl.WEB_SESSION_KEY);
//        if (seaUserObj instanceof AuthUser) {
//            return (AuthUser) seaUserObj;
//        }
//
//        return null;
//    }
//
//    public static final class SimpleWebAuthUserImpl implements AuthUser {
//
//        private String username;
//
//        public SimpleWebAuthUserImpl(String username) {
//            this.username = username;
//        }
//
//        @Override
//        public boolean authTarget(String target, PrivilegeType privilegeType) {
//            return true;
//        }
//
//        @Override
//        public boolean isSuperUser() {
//            return true;
//        }
//
//        @Override
//        public String getNickName() {
//            return username;
//        }
//
//        @Override
//        public String getLoginName() {
//            return username;
//        }
//
//        @Override
//        public String getId() {
//            return username;
//        }
//    }
//}
