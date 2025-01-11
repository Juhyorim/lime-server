package com.lime.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lime.server.BusApiTest;
import com.lime.server.busApi.dto.apiResponse.BusArriveApiResponse;
import com.lime.server.busApi.dto.apiResponse.BusArriveApiResponse.ArriveBus;
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
public class BusArriveApiTest {
    @Value("${api.serviceKey}")
    public String serviceKey;

    static String CITY_CODE_API_URL = "http://apis.data.go.kr/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList";
    static final Logger logger = LoggerFactory.getLogger(BusApiTest.class);

    @Test
    void arriveApiTest() throws IOException {
        StringBuilder urlBuilder = new StringBuilder(CITY_CODE_API_URL);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append(
                "&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append(
                "&" + URLEncoder.encode("cityCode", "UTF-8") + "=" + URLEncoder.encode("37050", "UTF-8"));
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
        ObjectMapper objectMapper = new ObjectMapper();
        BusArriveApiResponse cityCodeApiResponse = objectMapper.readValue(sb.toString(), BusArriveApiResponse.class);

        if ("00".equals(cityCodeApiResponse.getResponse().getHeader().getResultCode())) {
            BusArriveApiResponse.Items busArriveApiItems = cityCodeApiResponse.getResponse().getBody().getItems();
            List<ArriveBus> cities = busArriveApiItems.getItem();
            for (ArriveBus bus : cities) {
                logger.info(() ->
                        "arrprevstationcnt: " + bus.getArrprevstationcnt()
                                + ", arrtime: " + bus.getArrtime()
                                + ", nodeid: " + bus.getNodeid()
                                + ", nodenm: " + bus.getNodenm()
                                + ", routeid: " + bus.getRouteid()
                                + ", routeno: " + bus.getRouteno() //버스번호
                                + ", routetp: " + bus.getRoutetp()
                                + ", vehicletp: " + bus.getVehicletp()
                );
            }
        }
    }
}
