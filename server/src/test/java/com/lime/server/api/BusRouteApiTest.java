package com.lime.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lime.server.BusApiTest;
import com.lime.server.busApi.dto.apiResponse.BusRouteApiResponse;
import com.lime.server.busApi.dto.apiResponse.BusRouteApiResponse.BusStation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BusRouteApiTest {
    @Value("${api.serviceKey}")
    public String serviceKey;

    static String BUS_ROUTE_API_URL = "http://apis.data.go.kr/1613000/BusRouteInfoInqireService/getRouteAcctoThrghSttnList";
    static final Logger logger = LoggerFactory.getLogger(BusApiTest.class);

    @Test
    void busRouteApiTest() throws IOException {
        //GMB18730
        StringBuilder urlBuilder = new StringBuilder(BUS_ROUTE_API_URL);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append(
                "&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));
urlBuilder.append(
                "&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append(
                "&" + URLEncoder.encode("cityCode", "UTF-8") + "=" + URLEncoder.encode("37050", "UTF-8"));
        urlBuilder.append(
                "&" + URLEncoder.encode("routeId", "UTF-8") + "=" + URLEncoder.encode("GMB18730", "UTF-8"));

        logger.info(() -> "url: " + urlBuilder);
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        int responseCode = conn.getResponseCode();
        logger.info(() -> "Response code: " + responseCode);

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

//        logger.info(() -> sb.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        BusRouteApiResponse BusRouteApiResponse = objectMapper.readValue(sb.toString(), BusRouteApiResponse.class);

        if ("00".equals(BusRouteApiResponse.getResponse().getHeader().getResultCode())) {
            BusRouteApiResponse.Items busArriveApiItems = BusRouteApiResponse.getResponse().getBody().getItems();
            List<BusStation> busStations = busArriveApiItems.getItem();
            for (BusStation bus : busStations) {
                logger.info(() ->
                        "routeid: " + bus.getRouteid()
                                + ", nodeid: " + bus.getNodeid()
                                + ", nodenm: " + bus.getNodenm()
                                + ", nodeno: " + bus.getNodeno()
                                + ", nodeord: " + bus.getNodeord()
                                + ", gpslati: " + bus.getGpslati() //버스번호
                                + ", gpslong: " + bus.getGpslong()
                                + ", updowncd: " + bus.getUpdowncd()
                );
            }
        }
    }
}