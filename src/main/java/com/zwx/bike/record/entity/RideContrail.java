package com.zwx.bike.record.entity;

import com.zwx.bike.bike.entity.Point;
import lombok.Data;

import java.util.List;

/**
 * Create By Zhang on 2018/4/1
 */
@Data
public class RideContrail {
    private String rideRecordNo;

    private Long bikeNo;

    private List<Point> contrail;
}
