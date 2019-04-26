package com.zwx.bike.wallet.service;

import com.zwx.bike.wallet.entity.Wallet;

/**
 * @program: bike
 * @description:
 * @author: Zwx
 * @create: 2019-04-26 21:58
 **/
public interface WalletService {
    Wallet findByUserId(Long userId);
}
