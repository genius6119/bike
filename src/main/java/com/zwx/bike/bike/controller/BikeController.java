package com.zwx.bike.bike.controller;

import com.zwx.bike.bike.entity.Bike;
import com.zwx.bike.bike.entity.BikeLocation;
import com.zwx.bike.bike.entity.Point;
import com.zwx.bike.bike.service.BikeGeoService;
import com.zwx.bike.bike.service.BikeService;
import com.zwx.bike.common.constants.Constants;
import com.zwx.bike.common.exception.BikeException;
import com.zwx.bike.common.resp.ApiResult;
import com.zwx.bike.common.rest.BaseController;
import com.zwx.bike.record.entity.RideContrail;
import com.zwx.bike.record.entity.RideRecord;
import com.zwx.bike.user.entity.UserElement;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Create By Zhang on 2018/3/24
 */
@Api(value = "SayController|一个用来测试swagger注解的控制器")
@RestController
@RequestMapping("bike")
@Slf4j
public class BikeController extends BaseController {
    @Autowired
    @Qualifier("bikeServiceImpl")
    private BikeService bikeService;

    @Autowired
    private BikeGeoService bikeGeoService;


    /**
     * 项目启动之前模拟生成单车
     */

//    @RequestMapping("/generateBike")
//    public ApiResult generateBike() {
//
//        ApiResult<String> resp = new ApiResult<>();
//        try {
//            bikeService.generateBike();
//            resp.setMessage("创建单车成功");
//        } catch (BikeException e) {
//            resp.setCode(e.getStatusCode());
//            resp.setMessage(e.getMessage());
//        } catch (Exception e) {
//            log.error("Fail to update bike info", e);
//            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
//            resp.setMessage("内部错误");
//        }
//        return resp;
//    }

    /**
     * 查询附近单车
     */
    @ApiOperation(value = "按照当前经纬度查询附件单车", notes = "")
    @RequestMapping(value = "/findAroundBike",method = RequestMethod.POST)
    public ApiResult findAroundBike(@RequestBody Point point) {

        ApiResult<List<BikeLocation>> resp = new ApiResult<>();
        try {
            List<BikeLocation> bikeList = bikeGeoService.geoNear("bike-position", null, point, 10, 500);
            resp.setMessage("查询附近单车成功");
            resp.setData(bikeList);
        } catch (BikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to find around bike info", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }

    /**
     * 解锁单车
     */
    @ApiOperation(value = "解锁单车", notes = "")
    @RequestMapping(value = "/unLockBike",method = RequestMethod.POST)
    public ApiResult unLockBike(@RequestBody Bike bike) {

        ApiResult<List<BikeLocation>> resp = new ApiResult<>();
        try {
            UserElement ue = getCurrentUser();
            bikeService.unLockBike(ue, bike.getNumber());
            resp.setMessage("等待单车解锁");
        } catch (BikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to unlock bike ", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }

    /**
     * 锁定单车
     */

    @RequestMapping(value = "/lockBike",method = RequestMethod.POST)
    @ApiOperation(value = "骑完单车锁定单车", notes = "")
    public ApiResult lockBike(@RequestBody BikeLocation bikeLocation) {

        ApiResult<List<BikeLocation>> resp = new ApiResult<>();
        try {
            bikeService.lockBike(bikeLocation);
            resp.setMessage("锁车成功");
        } catch (BikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to lock bike", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }

    /**
     * 单车上报坐标
     */
    @RequestMapping(value = "/reportLocation",method = RequestMethod.POST)
    @ApiOperation(value = "单车上报坐标(每5秒)", notes = "")
    public ApiResult reportLocation(@RequestBody BikeLocation bikeLocation) {

        ApiResult<List<BikeLocation>> resp = new ApiResult<>();
        try {
            bikeService.reportLocation(bikeLocation);
            resp.setMessage("上报坐标成功");
        } catch (BikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to report location", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }

    /**
     * 按订单号查询轨迹
     */
    @RequestMapping(value = "/rideContrail",method = RequestMethod.POST)
    @ApiOperation(value = "按订单号查询轨迹", notes = "")
    public ApiResult rideContrail(@RequestBody RideContrail rideContrail) {

        ApiResult<List<RideContrail>> resp = new ApiResult<>();
        try {
            RideContrail rc = bikeGeoService.rideContrail("ride_contrail", rideContrail.getRideRecordNo());
            resp.setObject(rc.getContrail());
            resp.setMessage("查询成功");
        } catch (BikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to find rideContrail ", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }
}
