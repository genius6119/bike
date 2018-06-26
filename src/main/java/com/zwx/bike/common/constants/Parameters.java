package com.zwx.bike.common.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Create By Zhang on 2018/3/11
 */
@Component
@Data
public class Parameters {


    /*****redis config start*******/
    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${redis.max-idle}")
    private int redisMaxTotal;
    @Value("${redis.max-total}")
    private int redisMaxIdle;
    @Value("${redis.max-wait-millis}")
    private int redisMaxWaitMillis;
    /*****redis config end*******/

    @Value("#{'${security.noneSecurityPath}'.split(',')}")   /**按照逗号切分变量*/
    private List<String> noneSecurityPath;
}
