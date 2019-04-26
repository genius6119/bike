package com.zwx.bike.bike.service;

import com.mongodb.*;
import com.zwx.bike.bike.entity.BikeLocation;
import com.zwx.bike.bike.entity.Point;
import com.zwx.bike.common.exception.BikeException;
import com.zwx.bike.record.entity.RideContrail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By Zhang on 2018/3/25
 *
 * 参考https://docs.mongodb.com/manual/reference/operator/query/nearSphere/#op._S_nearSphere
 *
 */
@Component
@Slf4j
public class BikeGeoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**查找单车并按距离排序*/
    public List<BikeLocation> geoNearSphere(String collection, String locationField,
                                           Point point, int minDistance, int maxDistance,
                                           DBObject query,DBObject fields,int limit) throws BikeException {

        try {
            if(query==null){
                query=new BasicDBObject();
            }
            /**拼接mongoDB查询语句*/
            query.put(locationField,
                    new BasicDBObject("$nearSphere",
                            new BasicDBObject("$geometry",
                                    new BasicDBObject("type", "Point")
                                            .append("coordinates", new double[]{point.getLongitude(), point.getLatitude()}))
                                            .append("$minDistance", minDistance)
                                            .append("$maxDistance", maxDistance)
                    ));
            query.put("status",1);
            /**查询结果转到list中*/
            List<DBObject> objList = mongoTemplate.getCollection(collection).find(query, fields).limit(limit).toArray();
            /**list转java对象*/
            List<BikeLocation> result=new ArrayList<>();
            for (DBObject obj:objList){
                BikeLocation location=new BikeLocation();
                location.setBikeNumber(((Integer)obj.get("bike_no")).longValue());
                location.setStatus((Integer)obj.get("status"));

                BasicDBList coordinates = (BasicDBList) ((BasicDBObject) obj.get("location")).get("coordinates");
                Double[] temp = new Double[2];
                coordinates.toArray(temp);
                location.setCoordinates(temp);
                result.add(location);

            }
            return result;
        }catch (Exception e){
            log.error("fail to find bike");
            throw  new BikeException("查找附近单车失败");
        }

    }


    /**
     * 查找单车并计算出距离
     * */
    public List<BikeLocation> geoNear(String collection, DBObject query, Point point, int limit, long maxDistance) throws BikeException {
        try {
            if (query == null) {
                query = new BasicDBObject();
            }
            List<DBObject> pipeLine = new ArrayList<>();
            BasicDBObject aggregate = new BasicDBObject("$geoNear",
                    new BasicDBObject("near", new BasicDBObject("type", "Point").append("coordinates", new double[]{point.getLongitude(), point.getLatitude()}))
                            .append("distanceField", "distance")
                            .append("num", limit)
                            .append("maxDistance", maxDistance)
                            .append("spherical", true)  /**是否需要2dsphere*/
                            .append("query", new BasicDBObject("status", 1))
            );
            pipeLine.add(aggregate);
            Cursor cursor = mongoTemplate.getCollection(collection).aggregate(pipeLine, AggregationOptions.builder().build()); /**返回一个游标*/
            List<BikeLocation> result = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                BikeLocation location = new BikeLocation();
                location.setBikeNumber(((Integer) obj.get("bike_no")).longValue());
                BasicDBList coordinates = (BasicDBList) ((BasicDBObject) obj.get("location")).get("coordinates");
                Double[] temp = new Double[2];
                coordinates.toArray(temp);
                location.setCoordinates(temp);
                location.setDistance((Double) obj.get("distance"));

                result.add(location);
            }

            return result;
        } catch (Exception e) {
            log.error("fail to find around bike", e);
            throw new BikeException("查找附近单车失败");
        }
    }

    /**查询轨迹*/

    public RideContrail rideContrail(String collection, String recordNo) throws BikeException {
        try {
            DBObject obj = mongoTemplate.getCollection(collection).findOne(new BasicDBObject("record_no", recordNo));
            RideContrail rideContrail = new RideContrail();
            rideContrail.setRideRecordNo((String) obj.get("record_no"));
            rideContrail.setBikeNo((Long) obj.get("bike_no"));
            BasicDBList locList = (BasicDBList) obj.get("contrail");
            List<Point> pointList = new ArrayList<>();
            for (Object object : locList) {
                BasicDBList locObj = (BasicDBList) ((BasicDBObject) object).get("loc");
                Double[] temp = new Double[2];
                locObj.toArray(temp);
                Point point = new Point(temp);
                pointList.add(point);
            }
            rideContrail.setContrail(pointList);
            return rideContrail;
        } catch (Exception e) {
            log.error("fail to query ride contrail", e);
            throw new BikeException("查询单车轨迹失败");
        }
    }
}
