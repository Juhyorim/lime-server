//package com.lime.server.api;
//
//import com.lime.server.BusApiTest;
//import com.lime.server.busApi.service.BusApiService;
//import java.io.IOException;
//import org.junit.jupiter.api.Test;
//import org.junit.platform.commons.logging.Logger;
//import org.junit.platform.commons.logging.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class BusArriveApiTest {
//    static final Logger logger = LoggerFactory.getLogger(BusApiTest.class);
//
//    @Autowired
//    private BusApiService busApiService;
//
//    @Test
//    void arriveApiTest() throws IOException {
//        //예시 API
//        //{"response":{"header":{"resultCode":"00","resultMsg":"NORMAL SERVICE."},"body":{"items":{"item":[{"arrprevstationcnt":3,"arrtime":225,"nodeid":"GMB620","nodenm":"옥계대우아파트앞","routeid":"GMB19120","routeno":191,"routetp":"일반버스","vehicletp":"저상버스"},{"arrprevstationcnt":5,"arrtime":304,"nodeid":"GMB620","nodenm":"옥계대우아파트앞","routeid":"GMB90020","routeno":900,"routetp":"일반버스","vehicletp":"일반차량"},{"arrprevstationcnt":2,"arrtime":158,"nodeid":"GMB620","nodenm":"옥계대우아파트앞","routeid":"GMB96020","routeno":960,"routetp":"좌석버스","vehicletp":"일반차량"}]},"numOfRows":10,"pageNo":1,"totalCount":3}}}
//        busApiService.getArriveBuses("37050", "GMB620"); //GMB4
//    }
//}
