package com.zwx.bike.record.controller;


import com.zwx.bike.bike.service.BikeGeoService;
import com.zwx.bike.common.constants.Constants;
import com.zwx.bike.common.exception.BikeException;
import com.zwx.bike.common.resp.ApiResult;
import com.zwx.bike.common.rest.BaseController;
import com.zwx.bike.record.entity.RideRecord;
import com.zwx.bike.record.service.RideRecordService;
import com.zwx.bike.user.entity.UserElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Create By Zhang on 2018/3/31
 */
@RestController
@RequestMapping("rideRecord")
@Slf4j
public class RideRecordController extends BaseController {

    @Autowired
    @Qualifier("rideRecordServiceImpl")
    private RideRecordService rideRecordService;
    @Autowired
    private BikeGeoService bikeGeoService;


    /**
     * 查询骑行历史 分页
     */

    @RequestMapping("/list/{id}")
    public ApiResult<List<RideRecord>> listRideRecord(@PathVariable("id") Long lastId){

        ApiResult<List<RideRecord>> resp = new ApiResult<>();
        try {
            UserElement ue = getCurrentUser();
            List<RideRecord> list = rideRecordService.listRideRecord(ue.getUserId(),lastId);
            resp.setData(list);
            resp.setMessage("查询成功");
        } catch (BikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to query ride record ", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }



}