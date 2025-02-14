package com.lime.server.api;

import com.lime.server.BusApiTest;
import com.lime.server.busApi.service.BusApiService;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BusArriveApiTest {
    static final Logger logger = LoggerFactory.getLogger(BusApiTest.class);

    @Autowired
    private BusApiService busApiService;

    @Test
    void arriveApiTest() throws IOException {
        busApiService.getArriveBuses("37050", "GMB4");
    }
}
