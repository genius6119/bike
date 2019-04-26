package com.zwx.bike.wallet.controller;

import com.zwx.bike.common.constants.Constants;
import com.zwx.bike.common.resp.ApiResult;
import com.zwx.bike.common.rest.BaseController;
import com.zwx.bike.user.entity.UserElement;
import com.zwx.bike.wallet.entity.Wallet;
import com.zwx.bike.wallet.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: bike
 * @description:
 * @author: Zwx
 * @create: 2019-04-26 21:55
 **/
@Slf4j
@RestController
@RequestMapping("wallet")
public class WallectController extends BaseController {

    @Autowired
    @Qualifier("walletServiceImpl")
    private WalletService walletService;

    @RequestMapping(value = "/getUserWallet",method = RequestMethod.GET)
    public ApiResult<Wallet> getWalletInfo(){
        ApiResult<Wallet> resp=new ApiResult<>();
        UserElement ue=getCurrentUser();

        if(ue == null){
            resp.setCode(Constants.RESP_STATUS_NOAUTH);
            resp.setMessage("请登录");
            return resp;
        }

        try {
            resp.setData(walletService.findByUserId(ue.getUserId()));
            resp.setMessage("获取成功");
        }catch (Exception e){
            log.error("获取钱包信息异常",e);
        }

        return resp;
    }
}
