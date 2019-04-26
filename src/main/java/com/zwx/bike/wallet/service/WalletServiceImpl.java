package com.zwx.bike.wallet.service;

import com.zwx.bike.wallet.dao.WalletMapper;
import com.zwx.bike.wallet.entity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: bike
 * @description:
 * @author: Zwx
 * @create: 2019-04-26 22:04
 **/
@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    WalletMapper walletMapper;

    @Override
    public Wallet findByUserId(Long userId) {
        return walletMapper.selectByUserId(userId);
    }
}
