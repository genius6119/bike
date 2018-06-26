package com.zwx.bike.security;


import org.springframework.security.core.AuthenticationException;

/**
 * Create By Zhang on 2018/3/18
 */
public class BadCredentialException extends AuthenticationException {
    public BadCredentialException(String msg) {
        super(msg);
    }
}
