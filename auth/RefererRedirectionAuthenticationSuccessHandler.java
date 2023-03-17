package com.shopee.seamiter.auth;

import com.shopee.seamiter.domain.AuthUser;
import com.shopee.seamiter.service.AuthUserService;
import com.shopee.seamiter.util.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.shopee.seamiter.auth.DefaultLoginAuthenticationFilter.WEB_SESSION_KEY;

/**
 * @author honggang.liu
 */
@Component
public class RefererRedirectionAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    public static final String DEFAULT_ROLE_NAME = "TEST_USER_ROLE";

    /**
     * 用户服务
     */
    @Resource
    private AuthUserService authUserService;

    public RefererRedirectionAuthenticationSuccessHandler() {
        super();
        setUseReferer(true);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
        AuthUser authUser = authUserService.loadByUserName(oauthUser.getEmail());
        if (authUser == null) {
            authUser = new AuthUser();
            authUser.setRoleName(DEFAULT_ROLE_NAME);
            authUser.setPermissionPlatforms("");
            authUser.setUsername(oauthUser.getEmail());
            authUser.setEmail(oauthUser.getEmail());
            authUser.setAddTime(new Date());
            authUser.setUpdateTime(new Date());
            authUserService.save(authUser);
        }
        authUser.setAuthToken(JwtUtils.createToken(authUser));
        // do login
        request.getSession().setAttribute(WEB_SESSION_KEY, authUser);

        response.sendRedirect("/index.html");
    }

}
