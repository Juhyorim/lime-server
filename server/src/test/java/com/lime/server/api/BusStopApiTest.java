package com.lime.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lime.server.BusApiTest;
import com.lime.server.bus.dto.apiResponse.BusStopApiResponse;
import com.lime.server.bus.dto.apiResponse.BusStopApiResponse.BusStop;
import com.lime.server.bus.dto.apiResponse.BusStopRouteApiResponse;
import com.lime.server.bus.dto.apiResponse.BusStopRouteApiResponse.BusRoute;
import com.lime.server.bus.dto.apiResponse.CityApiResponse;
import com.lime.server.bus.dto.apiResponse.CityApiResponse.City;
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
public class BusStopApiTest {
    @Value("${api.serviceKey}")
    public String serviceKey;

    static String CITY_CODE_API_URL = "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getCtyCodeList";
    static String BUS_STOP_API_URL = "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getSttnNoList";
    static String BUS_STOP_ROUTE_API_URL = "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getSttnThrghRouteList";

    static final Logger logger = LoggerFactory.getLogger(BusApiTest.class);

    @Test
    void cityCodeApiTest() throws IOException {
        //City Code: 37050, City Name: 구미시
        StringBuilder urlBuilder = new StringBuilder(CITY_CODE_API_URL);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append(
                "&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));

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

        ObjectMapper objectMapper = new ObjectMapper();
        CityApiResponse cityCodeApiResponse = objectMapper.readValue(sb.toString(), CityApiResponse.class);

        if ("00".equals(cityCodeApiResponse.getResponse().getHeader().getResultCode())) {
            CityApiResponse.Items cityApiItems = cityCodeApiResponse.getResponse().getBody().getItems();
            List<City> cities = cityApiItems.getItem();
            for (City city : cities) {
                logger.info(() -> "City Code: " + city.getCitycode() + ", City Name: " + city.getCityname());
            }
        }
    }

    @Test
    void busStopApiTest() throws IOException {
        StringBuilder urlBuilder = new StringBuilder(BUS_STOP_API_URL);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append(
                "&" + URLEncoder.encode("cityCode", "UTF-8") + "=" + URLEncoder.encode("37050", "UTF-8"));
        urlBuilder.append(
                "&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));

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

        //매핑 제대로 다시
        ObjectMapper objectMapper = new ObjectMapper();
        BusStopApiResponse cityCodeApiResponse = objectMapper.readValue(sb.toString(), BusStopApiResponse.class);

        if ("00".equals(cityCodeApiResponse.getResponse().getHeader().getResultCode())) {
            BusStopApiResponse.Items items = (BusStopApiResponse.Items) cityCodeApiResponse.getResponse().getBody().getItems();
            List<BusStop> cities = items.getItem();
            for (BusStop busStop : cities) {
                logger.info(() -> "node id: " + busStop.getNodeid() + ", nodenm: " + busStop.getNodenm() + ", nodeno: " + busStop.getNodeno());
            }
        }
    }

    @Test
    void busStopRouteApiTest() throws IOException {
        StringBuilder urlBuilder = new StringBuilder(BUS_STOP_ROUTE_API_URL);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append(
                "&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));
        urlBuilder.append(
                "&" + URLEncoder.encode("cityCode", "UTF-8") + "=" + URLEncoder.encode("37050", "UTF-8"));
        urlBuilder.append(
                "&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append(
                "&" + URLEncoder.encode("nodeId", "UTF-8") + "=" + URLEncoder.encode("GMB708", "UTF-8"));

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

        //매핑 제대로 다시
        ObjectMapper objectMapper = new ObjectMapper();
        BusStopRouteApiResponse cityCodeApiResponse = objectMapper.readValue(sb.toString(), BusStopRouteApiResponse.class);

        if ("00".equals(cityCodeApiResponse.getResponse().getHeader().getResultCode())) {
            BusStopRouteApiResponse.Items items = (BusStopRouteApiResponse.Items) cityCodeApiResponse.getResponse().getBody().getItems();
            List<BusRoute> busRoutes = items.getItem();
            for (BusRoute route : busRoutes) {
                logger.info(() ->
                        "routeid id: " + route.getRouteid()
                                + ", routeno: " + route.getRouteno()
                                + ", routetp: " + route.getRoutetp()
                                + ", endnodenm: " + route.getEndnodenm()
                                + ", startnodenm: " + route.getStartnodenm()
                );
            }
        }
    }
}
