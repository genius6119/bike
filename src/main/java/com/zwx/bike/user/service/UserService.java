package com.zwx.bike.user.service;

import com.zwx.bike.common.exception.BikeException;
import com.zwx.bike.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * Create By Zhang on 2018/2/9
 */
public interface UserService {
    String login(String data, String key) throws BikeException;

    void modifyNickName(User user) throws BikeException;

    void sendVercode(String mobile, String ip) throws BikeException;

    String  uploadHeadImg(MultipartFile file, Long id) throws BikeException;

    User selectById(Long id);
}
