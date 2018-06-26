package com.zwx.bike.bike.dao;

import com.zwx.bike.bike.entity.Bike;
import com.zwx.bike.bike.entity.BikeNoGen;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BikeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Bike record);

    int insertSelective(Bike record);

    Bike selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Bike record);

    int updateByPrimaryKey(Bike record);

    void generataBikeNo(BikeNoGen bikeNoGen);


    Bike selectByBikeNo(Long bikeNo);
}