package com.zwx.bike.record.service;

import com.zwx.bike.common.exception.BikeException;
import com.zwx.bike.record.dao.RideRecordMapper;
import com.zwx.bike.record.entity.RideRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create By Zhang on 2018/3/31
 */
@Slf4j
@Service
public class RideRecordServiceImpl implements RideRecordService {
    @Autowired
    private RideRecordMapper rideRecordMapper;

    @Override
    public List<RideRecord> listRideRecord(long userId, Long lastId) throws BikeException {

        List<RideRecord> list = rideRecordMapper.selectRideRecordPage(userId,lastId);

        return list;
    }
}
