package com.zwx.bike.bike.entity;

import lombok.Data;

/**
 * Create By Zhang on 2018/3/25
 */
@Data
public class Point {
    public Point(){

    }
    public Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Point(Double[] loc) {
        this.longitude=loc[0];
        this.latitude=loc[1];

    }



    private double longitude;

    private double latitude;
}
