package com.zwx.bike.wallet.dao;

import com.zwx.bike.wallet.entity.Wallet;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper

public interface WalletMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Wallet record);

    int insertSelective(Wallet record);

    Wallet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Wallet record);

    int updateByPrimaryKey(Wallet record);

    Wallet selectByUserId(Long userId);
}