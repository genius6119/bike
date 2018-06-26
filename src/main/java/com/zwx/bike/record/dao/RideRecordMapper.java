package com.zwx.bike.record.dao;

import com.zwx.bike.record.entity.RideRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface RideRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RideRecord record);

    int insertSelective(RideRecord record);

    RideRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RideRecord record);

    int updateByPrimaryKey(RideRecord record);

    RideRecord selectRecordNotClosed(Long userId);

    RideRecord selectBikeRecordOnGoing(Long bikeNo);

    List<RideRecord> selectRideRecordPage(@Param("userId") long userId, @Param("lastId") Long lastId);
}