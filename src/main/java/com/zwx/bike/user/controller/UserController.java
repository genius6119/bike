package com.zwx.bike.user.controller;

import com.zwx.bike.common.constants.Constants;
import com.zwx.bike.common.exception.BikeException;
import com.zwx.bike.common.resp.ApiResult;
import com.zwx.bike.common.rest.BaseController;
import com.zwx.bike.user.entity.LoginInfo;
import com.zwx.bike.user.entity.User;
import com.zwx.bike.user.entity.UserElement;
import com.zwx.bike.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Create By Zhang on 2018/2/6
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController extends BaseController{
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    /**
     * @Author Zhang
     * @Date 2018/2/26 18:57
     * @Description  用户登录
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<String> login(@RequestBody LoginInfo loginInfo) {

        ApiResult<String> resp=new ApiResult<>();
        try {
            String data = loginInfo.getData();
            String key = loginInfo.getKey();
            if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {//检查是否相等且不为空
                throw new BikeException("参数校验失败");
            }
            String token = userService.login(data,key);
            resp.setData(token);
            }catch (BikeException e){
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage(e.getMessage());
        }catch (Exception e){
            log.error("Fail to Login",e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }
        return resp;
    }

    /**
     * @Author Zhang
     * @Date 2018/3/16 13:47
     * @Description 修改用户昵称
     */
    @RequestMapping(value = "/modifyNickName",method = RequestMethod.POST)
    public ApiResult modifyNickName(@RequestBody User user){
        ApiResult resp=new ApiResult();
        try{
            UserElement ue=getCurrentUser();           /**从redis中返回用户信息,而不是从前端*/
            user.setId(ue.getUserId());
            userService.modifyNickName(user);
        }catch (BikeException e){
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage(e.getMessage());
        }catch (Exception e){
            log.error("Fail to modifyNickName",e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }
        return resp;
    }


    /**
     * @Author Zhang
     * @Date 2018/3/26 18:56
     * @Description   发送验证码
     */
    @RequestMapping(value = "/sendVercode",method = RequestMethod.POST)
    public ApiResult sendVercode(@RequestBody User user, HttpServletRequest request){
        ApiResult resp=new ApiResult();
        try{
            userService.sendVercode(user.getMobile(),getIpFromRequest(request));
        }catch (BikeException e){
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage(e.getMessage());
        }catch (Exception e){
            log.error("Fail to send sms vercode",e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }
        return resp;
    }

    /**
     * @Author Zhang
     * @Date 2018/3/26 18:57
     * @Description 更改头像
     */
    @RequestMapping(value = "/uploadHeadImg", method = RequestMethod.POST)
    public ApiResult<String> uploadHeadImg(HttpServletRequest req, @RequestParam(required=false ) MultipartFile file) {

        ApiResult<String> resp = new ApiResult<>();
        try {
            UserElement ue = getCurrentUser();
            userService.uploadHeadImg(file,ue.getUserId());
            resp.setMessage("上传成功");
        } catch (BikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to update user info", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }
        return resp;
    }

}
