package com.zwx.bike.security;

import com.zwx.bike.user.entity.User;
import com.zwx.bike.user.entity.UserElement;
import lombok.Data;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Create By Zhang on 2018/3/18
 */
@Data
public class RestAuthenticationToken extends AbstractAuthenticationToken{

    public RestAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {  /**authorities查看用户所拥有的权限*/

        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
    private UserElement user;
}
