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
//public class BusStopApiTest {
//    @Autowired
//    private BusApiService busApiService;
//
//    static final Logger logger = LoggerFactory.getLogger(BusApiTest.class);
//
//    @Test
//    void cityCodeApiTest() throws IOException {
//        busApiService.getCities();
//    }
//
//    @Test
//    void busStopApiTest() throws IOException {
//        busApiService.getBusStations("37050", 1);
//    }
//
//    @Test
//    void busStopRouteApiTest() throws IOException {
//        busApiService.getBusRouteInfo("37050", "GMB4");
//    }
//}
