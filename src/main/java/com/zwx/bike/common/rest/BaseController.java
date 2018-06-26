package com.zwx.bike.common.rest;

import com.zwx.bike.cache.CommonCacheUtil;
import com.zwx.bike.common.constants.Constants;
import com.zwx.bike.user.entity.UserElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;


/**
 * Create By Zhang on 2018/3/16
 */
@Slf4j
public class BaseController {
    @Autowired
    private CommonCacheUtil cacheUtil;

    protected UserElement getCurrentUser(){     /**从redis中返回用户信息*/
        HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token=request.getHeader(Constants.REQUEST_TOKEN_KEY);
        if(!StringUtils.isBlank(token)) {
            try {
                UserElement ue = cacheUtil.getUserByToken(token);
                return ue;
            }catch (Exception e){
                log.error("fail to get user by token",e);
                throw e;
            }

        }
        return null;

    }

    /**获取IP*/
    protected String getIpFromRequest(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;      /**方便本机测试*/
    }
}
