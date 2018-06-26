package com.zwx.bike.bike.entity;

import lombok.Data;

/**
 * Create By Zhang on 2018/3/25
 */
@Data
public class BikeLocation {
    private String id;

    private Long bikeNumber;

    private int status;

    private Double[] coordinates;

    private Double distance;

}
