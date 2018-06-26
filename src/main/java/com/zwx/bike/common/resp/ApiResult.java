package com.zwx.bike.common.resp;

import com.zwx.bike.common.constants.Constants;
import lombok.Data;

/**
 * Create By Zhang on 2018/3/4
 */
@Data
public class ApiResult <T> {
    private int code= Constants.RESP_STATUS_OK;

    private String message;

    private T data;

    private  Object object;
}
