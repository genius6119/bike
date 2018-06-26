package com.zwx.bike.record.service;

import com.zwx.bike.common.exception.BikeException;
import com.zwx.bike.record.entity.RideRecord;

import java.util.List;

/**
 * Create By Zhang on 2018/3/31
 */
public interface RideRecordService {

    List<RideRecord> listRideRecord(long userId, Long lastId) throws BikeException;
}
