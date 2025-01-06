package com.lime.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lime.server.BusApiTest;
import com.lime.server.bus.dto.cityApi.CityCodeApiResponse;
import com.lime.server.bus.dto.cityApi.CityCodeDto;
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

    static final Logger logger = LoggerFactory.getLogger(BusApiTest.class);

    @Test
    void cityCodeApi() throws IOException {
        // 기존 코드에 추가할 매핑 로직
        StringBuilder urlBuilder = new StringBuilder(CITY_CODE_API_URL); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append(
                "&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*페이지번호*/
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

        // JSON을 객체로 매핑
        ObjectMapper objectMapper = new ObjectMapper();
        CityCodeApiResponse cityCodeApiResponse = objectMapper.readValue(sb.toString(), CityCodeApiResponse.class);

        // 매핑된 객체 사용 예시
        if ("00".equals(cityCodeApiResponse.getResponse().getHeader().getResultCode())) {
            List<CityCodeDto> cities = cityCodeApiResponse.getResponse().getBody().getItems().getItem();
            for (CityCodeDto city : cities) {
                logger.info(() -> "City Code: " + city.getCitycode() + ", City Name: " + city.getCityname());
            }
        }
    }
}
