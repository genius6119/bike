package com.zwx.bike.common.exception;

import com.zwx.bike.common.constants.Constants;
import org.springframework.jca.cci.CciOperationNotSupportedException;

/**
 * Create By Zhang on 2018/3/11
 */
public class BikeException extends Exception{

    public BikeException(String message) {
        super(message);

    }

    public int getStatusCode(){
        return Constants.RESP_STATUS_INTERNAL_ERROR;
    }
}
