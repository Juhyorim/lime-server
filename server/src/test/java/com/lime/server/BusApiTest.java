package com.lime.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BusApiTest {
    @Value("${api.serviceKey}")
    public String serviceKey;

    static String GET_ROUTE_BUS_LIST_URL = "http://apis.data.go.kr/1613000/BusLcInfoInqireService/getRouteAcctoBusLcList";
    static String GET_ROUTE_ACCESS_BUS_INFO_URL = "http://apis.data.go.kr/1613000/BusLcInfoInqireService/getRouteAcctoSpcifySttnAccesBusLcInfo";
    static String GET_CITY_CODE_LIST_URL = "http://apis.data.go.kr/1613000/BusLcInfoInqireService/getCtyCodeList"; //도시코드 목록 조회

    static final Logger logger = LoggerFactory.getLogger(BusApiTest.class);

    @Test
    void ApiTest1() throws IOException {
        StringBuilder urlBuilder = new StringBuilder(GET_ROUTE_BUS_LIST_URL); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*데이터 타입(xml, json)*/
        urlBuilder.append("&" + URLEncoder.encode("cityCode","UTF-8") + "=" + URLEncoder.encode("25", "UTF-8")); /*도시코드 [상세기능3 도시코드 목록 조회]에서 조회 가능*/
        urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + URLEncoder.encode("DJB30300052", "UTF-8")); /*노선ID [국토교통부(TAGO)_버스노선정보]에서 조회가능*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        int responseCode = conn.getResponseCode();
        logger.info(() -> "Response code: " + responseCode);

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;

        sb.append("@================@").append("\n");
        while ((line = rd.readLine()) != null) {
            sb.append(line).append("\n");
        }
        rd.close();
        conn.disconnect();
        logger.info(() -> sb.toString());
    }

    @Test
    void ApiTest2() throws IOException {
        StringBuilder urlBuilder = new StringBuilder(GET_ROUTE_ACCESS_BUS_INFO_URL); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*데이터 타입(xml, json)*/
        urlBuilder.append("&" + URLEncoder.encode("cityCode","UTF-8") + "=" + URLEncoder.encode("25", "UTF-8")); /*도시코드 [상세기능3 도시코드 목록 조회]에서 조회 가능*/
        urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + URLEncoder.encode("DJB30300052", "UTF-8")); /*노선ID [국토교통부(TAGO)_버스노선정보]에서 조회가능*/
        urlBuilder.append("&" + URLEncoder.encode("nodeId","UTF-8") + "=" + URLEncoder.encode("DJB8007268", "UTF-8")); /*정류소 ID [국토교통부(TAGO)_버스노선정보]에서 조회가능*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        int responseCode = conn.getResponseCode();
        logger.info(() -> "Response code: " + responseCode);

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;

        sb.append("@================@").append("\n");
        while ((line = rd.readLine()) != null) {
            sb.append(line).append("\n");
        }
        rd.close();
        conn.disconnect();
        logger.info(() -> sb.toString());
    }

    @Test
    void ApiTest3() throws IOException {
        StringBuilder urlBuilder = new StringBuilder(GET_CITY_CODE_LIST_URL); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*데이터 타입(xml, json)*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        int responseCode = conn.getResponseCode();
        logger.info(() -> "Response code: " + responseCode);

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;

        sb.append("@================@").append("\n");
        while ((line = rd.readLine()) != null) {
            sb.append(line);
//            ObjectMapper objectMapper = new ObjectMapper();
//            CityApiResponse cityApiResponse = objectMapper.readValue(line, CityApiResponse.class);
//            List<City> cities = cityApiResponse.getResponse().getBody().getItems().getItem();
//
//            for (City city: cities) {
//                sb.append(city.getCityname()).append("   ").append(city.getCitycode()).append("\n");
////                this.ApiTest1(city.getCitycode());
//            }
        }
        rd.close();
        conn.disconnect();
        logger.info(() -> sb.toString());
    }
}
