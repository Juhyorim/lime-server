package com.lime.server.busApi.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lime.server.busApi.dto.apiResponse.BusArriveApiResponse;
import com.lime.server.busApi.dto.apiResponse.BusArriveApiResponse.ArriveBus;
import com.lime.server.busApi.dto.apiResponse.BusStopApiResponse;
import com.lime.server.busApi.dto.apiResponse.BusStopRouteApiResponse;
import com.lime.server.busApi.dto.apiResponse.CityApiResponse;
import com.lime.server.busApi.dto.apiResponse.CityApiResponse.City;
import com.lime.server.tico.dto.response.BusRouteResponse;
import com.lime.server.tico.dto.response.BusStationResponse;
import com.lime.server.tico.dto.response.CityResponse;
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
    private static final String BUS_STOP_API_URL_FORMAT = "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getSttnNoList?serviceKey=%s&cityCode=%s&_type=json&numOfRows=50&pageNo=%d";
    private static final String BUS_STOP_ROUTE_API_URL = "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getSttnThrghRouteList";
    private static final String ARRIVE_BUS_API_URL = "http://apis.data.go.kr/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList";

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

    public BusStationResponse getBusStations(String cityCode, int pageNum) throws IOException {
        URL url = new URL(String.format(BUS_STOP_API_URL_FORMAT, serviceKey, cityCode, pageNum));
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
        BusStopApiResponse cityCodeApiResponse = objectMapper.readValue(sb.toString(), BusStopApiResponse.class);

//        if ("00".equals(cityCodeApiResponse.getResponse().getHeader().getResultCode())) {
//        }

        return BusStationResponse.from(cityCodeApiResponse);
    }

    public BusRouteResponse getBusRouteInfo(String cityCode, String nodeId) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(BUS_STOP_ROUTE_API_URL);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append(
                "&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));
        urlBuilder.append(
                "&" + URLEncoder.encode("cityCode", "UTF-8") + "=" + URLEncoder.encode(cityCode, "UTF-8"));
        urlBuilder.append(
                "&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append(
                "&" + URLEncoder.encode("nodeid", "UTF-8") + "=" + URLEncoder.encode(nodeId, "UTF-8"));

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
        BusStopRouteApiResponse busStopRouteApiResponse = objectMapper.readValue(sb.toString(),
                BusStopRouteApiResponse.class);

//        if ("00".equals(busStopRouteApiResponse.getResponse().getHeader().getResultCode())) {
//        }

        return BusRouteResponse.from(busStopRouteApiResponse);
    }

    public void getArriveBuses() throws IOException {
        StringBuilder urlBuilder = new StringBuilder(ARRIVE_BUS_API_URL);
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
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true); //빈 문자열이 올 때(도착 예정 버스가 없을 때) 처리
        BusArriveApiResponse cityCodeApiResponse = objectMapper.readValue(sb.toString(), BusArriveApiResponse.class);

        if ("00".equals(cityCodeApiResponse.getResponse().getHeader().getResultCode())) {
            BusArriveApiResponse.Items busArriveApiItems = cityCodeApiResponse.getResponse().getBody().getItems();

            if (busArriveApiItems == null) {
                return;
            }

            List<ArriveBus> cities = busArriveApiItems.getItem();
            for (ArriveBus bus : cities) {
                log.info(
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
