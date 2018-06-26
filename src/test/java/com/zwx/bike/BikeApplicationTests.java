package com.zwx.bike;

import com.zwx.bike.bike.dao.BikeMapper;
import com.zwx.bike.bike.entity.BikeLocation;
import com.zwx.bike.bike.entity.Point;
import com.zwx.bike.bike.service.BikeGeoService;
import com.zwx.bike.bike.service.BikeService;
import com.zwx.bike.common.exception.BikeException;
import com.zwx.bike.user.entity.UserElement;
import com.zwx.bike.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = BikeApplication.class,webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BikeApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;
	@LocalServerPort
	private int port;
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;

	@Autowired
	@Qualifier("bikeServiceImpl")
	private BikeService bikeService;
	@Autowired
	private BikeGeoService bikeGeoService;


	@Test
	public void contextLoads() {
		String result=restTemplate.getForObject("/user/hello",String.class);
		System.out.println(result);
	}
	/**查找符合条件范围内的单车*/
//	@Test
//	public void geoText() throws BikeException {
////		bikeGeoService.geoNearSphere("bike-position","location",new Point(120.914348,
////				32.026251),0,1000,null,null,10);
//		bikeGeoService.geoNear("bike-position",null,new Point(120.914348,
//				32.026251),5,500);
//	}

	/**查询单车轨迹*/
	@Test
	public void rideContrailTest() throws BikeException{
		bikeGeoService.rideContrail("ride_contrail","3045161480947907825488");
	}

	/**解锁，骑车测试*/
//	@Test
	//28000003
//	public void unlockTest ()throws BikeException{
//		UserElement ue=new UserElement();
//		ue.setUserId(1l);
//		ue.setPushChannelId("12345");
//		ue.setPlatform("android");
//		bikeService.unLockBike(ue,28000004l);
//
//	}
//
	/**锁车测试*/
//	@Test
//	//28000003
//	public void lockTest ()throws BikeException{
//		UserElement ue=new UserElement();
//		ue.setUserId(1l);
//		ue.setPushChannelId("12345");
//		ue.setPlatform("android");
//		BikeLocation bl=new BikeLocation();
//		bl.setStatus(2);
//		bl.setId("1");
//		bl.setBikeNumber(28000004l);
//		bl.setCoordinates(new Double[]{120.915,32.025});
//		bikeService.lockBike(bl);
//	}

	/**上报坐标测试*/
//	@Test
//	public  void reportLocationTest() throws BikeException{
//		BikeLocation bl=new BikeLocation();
//		bl.setBikeNumber(28000004l);
//		bl.setCoordinates(new Double[]{120.920155,32.02155});
//		bikeService.reportLocation(bl);
//	}


}
