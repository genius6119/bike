package com.zwx.bike.bike.service;

import com.zwx.bike.bike.entity.BikeLocation;
import com.zwx.bike.common.exception.BikeException;
import com.zwx.bike.user.entity.UserElement;

/**
 * Create By Zhang on 2018/3/24
 */
public interface BikeService {
    void generateBike() throws BikeException;

    void unLockBike(UserElement currentUser, Long number)throws BikeException;

    void lockBike(BikeLocation number) throws BikeException;

    void reportLocation(BikeLocation bikeLocation) throws BikeException;
}
