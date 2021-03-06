package com.zwx.bike.security;


import com.zwx.bike.cache.CommonCacheUtil;
import com.zwx.bike.common.constants.Constants;
import com.zwx.bike.user.entity.UserElement;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.util.AntPathMatcher;
import lombok.extern.slf4j.Slf4j;


import javax.servlet.http.HttpServletRequest;
;

import java.util.Arrays;
import java.util.List;



/**
 * Create By Zhang on 2018/3/17
 * 这个类是自定义的预授权过滤器,妈个鸡名字真长
 */
@Slf4j
public class RestPreAuthentucatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {
    /**校验用户提交的信息，交给provider*/

    /**
     * spring的路径匹配器
     */
    private AntPathMatcher matcher = new AntPathMatcher();

    private List<String> noneSecurityList;
    private CommonCacheUtil commonCacheUtil;

    public RestPreAuthentucatedProcessingFilter(List<String> noneSecurityList, CommonCacheUtil commonCacheUtil) {
        this.noneSecurityList = noneSecurityList;
        this.commonCacheUtil = commonCacheUtil;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {             /**取得用户授权*/
        GrantedAuthority[] authorities = new GrantedAuthority[1];
        if (isNoneSecurity(request.getRequestURI().toString()) || "OPTIONS".equals(request.getMethod())) {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_SOME");
            authorities[0] = authority;
            return new RestAuthenticationToken(Arrays.asList(authorities));
        }
        /**检查token*/
        String version = request.getHeader(Constants.REQUEST_VERSION_KEY);
        String token = request.getHeader(Constants.REQUEST_TOKEN_KEY);
        if (version == null) {
            request.setAttribute("header-error", 400);
        }

        if (request.getAttribute("header-error") == null) {
            try {
                if (token != null && !token.trim().isEmpty()) {
                    UserElement ue = commonCacheUtil.getUserByToken(token);

                    if (ue instanceof UserElement) {
                        /**检查到token说明用户已经登录 授权给用户BIKE_CLIENT角色 允许访问*/
                        GrantedAuthority authority = new SimpleGrantedAuthority("BIKE_CLIENT");
                        authorities[0] = authority;
                        RestAuthenticationToken authToken = new RestAuthenticationToken(Arrays.asList(authorities));
                        authToken.setUser(ue);
                        return authToken;
                    } else {
                        /**token不对*/
                        request.setAttribute("header-error", 401);
                    }
                } else {
                    log.warn("Got no token from request header");
                    /**token不存在 告诉移动端 登录*/
                    request.setAttribute("header-error", 401);
                }
            } catch (Exception e) {
                log.error("Fail to authenticate user", e);
            }
        }
        if (request.getAttribute("header-error") != null) {
            /**请求头有错误  随便给个角色 让逻辑继续*/
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_NONE");
            authorities[0] = authority;
        }
        RestAuthenticationToken authToken = new RestAuthenticationToken(Arrays.asList(authorities));
        return authToken;
    }


    private boolean isNoneSecurity(String uri) {
        boolean result = false;
        if (this.noneSecurityList != null) {
            for (String pattern : this.noneSecurityList) {
                if (matcher.match(pattern, uri)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }


    /**
     * 这个用不到
     */
    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return null;
    }
}

