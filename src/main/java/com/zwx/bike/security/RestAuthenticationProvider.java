package com.zwx.bike.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * Create By Zhang on 2018/3/17
 */
public class RestAuthenticationProvider implements AuthenticationProvider{
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof PreAuthenticatedAuthenticationToken) {
            PreAuthenticatedAuthenticationToken preAuth = (PreAuthenticatedAuthenticationToken) authentication;
            RestAuthenticationToken sysAuth = (RestAuthenticationToken) preAuth.getPrincipal();/**拿到用户的token*/
            if (sysAuth.getAuthorities() != null && sysAuth.getAuthorities().size() > 0) {
                GrantedAuthority gauth = sysAuth.getAuthorities().iterator().next();
                if ("BIKE_CLIENT".equals(gauth.getAuthority())) {   /**如果权限为BIKE_CLIENT，放过去*/
                    return sysAuth;
                }else if ("ROLE_SOMEONE".equals(gauth.getAuthority())) {    /**如果权限为ROLE_SOMEONE，放过去*/
                    return sysAuth;
                }
            }
        }
        throw new BadCredentialException("unknown.error");     /**如果权限为unknown.error，抛出去，不管*/
    }

    /**支持什么方法的校验*/
    @Override
    public boolean supports(Class<?> authentication) {

        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication)||RestAuthenticationToken.class.isAssignableFrom(authentication);/**不太懂，详见class源码*/

    }
}
