package com.zwx.bike.bike.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.zwx.bike.bike.dao.BikeMapper;
import com.zwx.bike.bike.entity.Bike;
import com.zwx.bike.bike.entity.BikeLocation;
import com.zwx.bike.bike.entity.BikeNoGen;
import com.zwx.bike.common.exception.BikeException;
import com.zwx.bike.common.utils.DateUtil;
import com.zwx.bike.common.utils.RandomNumberCode;
import com.zwx.bike.fee.dao.RideFeeMapper;
import com.zwx.bike.fee.entity.RideFee;
import com.zwx.bike.record.dao.RideRecordMapper;
import com.zwx.bike.record.entity.RideRecord;
import com.zwx.bike.user.dao.UserMapper;
import com.zwx.bike.user.entity.User;
import com.zwx.bike.user.entity.UserElement;
import com.zwx.bike.wallet.dao.WalletMapper;
import com.zwx.bike.wallet.entity.Wallet;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create By Zhang on 2018/3/24
 */
@Service
@Slf4j
public class BikeServiceImpl implements BikeService {

    private static final byte NOT_VERYFY=1;   /**未认证*/

    private static final Object BIKE_UNLOCK=2;  /**单车解锁*/

    private static final Object BIKE_LOCK=1;    /**单车锁定*/

    private static final Byte RIDE_END=2;    /**骑行结束*/

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BikeMapper bikeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RideRecordMapper rideRecordMapper;

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private RideFeeMapper feeMapper;

    @Override
    /**生成单车方法*/
    public void generateBike() throws BikeException {
        /**单车编号生成SQL*/
        BikeNoGen bikeNoGen=new BikeNoGen();
        bikeMapper.generataBikeNo(bikeNoGen);
        /**生成单车*/
        Bike bike=new Bike();
        bike.setType((byte)2);
        bike.setNumber(bikeNoGen.getAutoIncNo());
        bikeMapper.insertSelective(bike);
    }

    @Override
    @Transactional
    public void unLockBike(UserElement currentUser, Long bikeNo) throws BikeException {
        /**
         * 1.到数据库校验用户是否已经认证（实名 押金）
         * 2.检验用户是否有正在进行的骑行记录
         * 3.检查钱包余额 是否大于1元
         * 4.推送单车，进行解锁
         * 5.进MongoDB，将单车状态修改为2
         * 6.建立骑行订单 记录骑行时间 （调用另一个接口——>记录骑行轨迹 单车上报坐标）
         * */

        /**1*/
        try {
            User user = userMapper.selectByPrimaryKey(currentUser.getUserId());
            if (user.getVerifyFlag() == NOT_VERYFY) {
                throw new BikeException("用户尚未认证");
            }

            /**2*/
            RideRecord record=rideRecordMapper.selectRecordNotClosed(currentUser.getUserId());
            if (record!=null){
                throw new BikeException("存在未关闭骑行订单");
            }

            /**3*/
            Wallet wallet=walletMapper.selectByUserId(currentUser.getUserId());
            if(wallet.getRemainSum().compareTo(new BigDecimal(1))<0){
                throw new BikeException("余额不足");
            }
            /**4
             * 暂时无法演示，需要百度推送到真正的安卓端,而且注册要钱*/
//          BaiduPushUtil.pushMsgToSingleDevice(currentUser,"{\"title\":\"TEST\",\"description\":\"Hello Baidu push!\"}");
            /**5*/
            Query query=Query.query(Criteria.where("bike_no").is(bikeNo));
            Update update=Update.update("status",BIKE_UNLOCK);
            mongoTemplate.updateFirst(query,update,"bike-position");

            /**6*/
            RideRecord rideRecord = new RideRecord();
            rideRecord.setBikeNo(bikeNo);
            String recordNo =System.currentTimeMillis() + RandomNumberCode.randomNo();     /**生成唯一订单记录*/
            rideRecord.setRecordNo(recordNo);
            rideRecord.setStartTime(new Date());
            rideRecord.setUserid(currentUser.getUserId());
            rideRecordMapper.insertSelective(rideRecord);
        } catch (Exception e) {
            log.error("fail to unlock bike ",e);
            throw new BikeException("解锁单车失败");
        }
    }


    /**
     * @Author Zhang
     * @Date 2018/3/30 15:40
     * @Description  锁车
     * @param
     */
    @Override
    @Transactional
    public void lockBike(BikeLocation bikeLocation) throws BikeException {
        /**结束订单 计算骑行时间 存订单
         *
         * 查询单车类型 查询计价信息
         *
         *修改MongoDB中的单车状态
         * */
        try {
            RideRecord record=rideRecordMapper.selectBikeRecordOnGoing(bikeLocation.getBikeNumber());
            if(record==null){
                throw new BikeException("骑行记录不存在");
            }
            Long userid=record.getUserid();
            Bike bike=bikeMapper.selectByBikeNo(bikeLocation.getBikeNumber());
            if(bike==null){
                throw new BikeException("单车不存在");
            }
            RideFee fee=feeMapper.selectBikeTypeFee(bike.getType());
            if(fee==null){
                throw new BikeException("计费信息异常");
            }
            /**算钱*/
            BigDecimal cost=BigDecimal.ZERO;
            record.setEndTime(new Date());
            record.setStatus(RIDE_END);
            Long min= DateUtil.getBetweenMin(new Date(),record.getStartTime());
            record.setRideTime(min.intValue());
            int minUnit=fee.getMinUnit();  /**时间计费单位*/
            int inMin=min.intValue(); /**骑行时间*/
            if(inMin/minUnit==0){
                /**骑行不足30分钟*/
                cost=fee.getFee();
            }else if(inMin%minUnit==0){
                /**骑行时间整除了30分钟*/
                cost=fee.getFee().multiply(new BigDecimal(inMin/minUnit));
            }else if(inMin%minUnit!=0){
                /**没整除 +1个时间单位收钱**/
                cost=fee.getFee().multiply(new BigDecimal(inMin/minUnit+1));
            }
            record.setRideCost(cost);
            rideRecordMapper.updateByPrimaryKeySelective(record);

            /**钱包扣费*/
            Wallet wallet=walletMapper.selectByUserId(userid);
            wallet.setRemainSum(wallet.getRemainSum().subtract(cost));
            walletMapper.updateByPrimaryKeySelective(wallet);
            /**单车锁定,更新坐标*/
            Query query=Query.query(Criteria.where("bike_no").is(bikeLocation.getBikeNumber()));
            Update update=Update.update("status",BIKE_LOCK)
                    .set("location.coordinates",bikeLocation.getCoordinates());
            mongoTemplate.updateFirst(query,update,"bike-position");
        } catch (BikeException e) {
            log.error("Fail to Lock Bike",e);
            throw new BikeException("锁定单车失败");
        }
    }

    /**
     * @Author Zhang
     * @Date 2018/4/3 10:24
     * @Description 上报坐标
     */
    @Override
    public void reportLocation(BikeLocation bikeLocation) throws BikeException {
        /**在数据库查询是否有未结束的订单*/
        try {
            RideRecord record=rideRecordMapper.selectBikeRecordOnGoing(bikeLocation.getBikeNumber());
            if(record==null){
                throw new BikeException("骑行记录不存在");
            }
            /**查询MongoDB，是否第一次插入坐标数据*/
            DBObject obj=mongoTemplate.getCollection("ride_contrail")
                    .findOne(new BasicDBObject("record_no",record.getRecordNo()));
            /**如果没有 插入 如果有 继续添加坐标*/
            if(obj==null){
                List<BasicDBObject> list=new ArrayList<>();
                BasicDBObject temp=new BasicDBObject("loc",bikeLocation.getCoordinates());
                list.add(temp);
                BasicDBObject insertObj=new BasicDBObject("record_no",record.getRecordNo())
                        .append("bike_no",record.getBikeNo())
                        .append("contail",list);
                mongoTemplate.insert(insertObj,"ride_contrail");
            }else {
                Query query=new Query(Criteria.where("record_no").is(record.getRecordNo()));
                Update update=new Update().push("contrail",new BasicDBObject("loc",bikeLocation.getCoordinates()));
                mongoTemplate.updateFirst(query,update,"ride_contrail");
            }
        } catch (BikeException e) {
            log.error("fail to report location",e);
            throw new BikeException("上传坐标失败");
        }


    }


}
