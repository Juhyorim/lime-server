package com.lime.server.busApi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lime.server.busApi.dto.apiResponse.CityApiResponse;
import com.lime.server.busApi.dto.apiResponse.CityApiResponse.City;
import com.lime.server.busApi.dto.response.CityResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BusApiService {
    private static final String CITY_CODE_API_URL = "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getCtyCodeList";

    @Value("${api.serviceKey}")
    public String serviceKey;

    public CityResponse getCities() throws IOException {
        StringBuilder urlBuilder = new StringBuilder(CITY_CODE_API_URL);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append(
                "&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        int responseCode = conn.getResponseCode();
        log.info("Response code: " + responseCode);

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
                log.info("City Code: " + city.getCitycode() + ", City Name: " + city.getCityname());
            }
        }

        return CityResponse.from(cityCodeApiResponse);
    }
}
