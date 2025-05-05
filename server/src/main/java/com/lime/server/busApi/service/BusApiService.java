package com.lime.server.busApi.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lime.server.busApi.dto.apiResponse.BusArriveApiResponse;
import com.lime.server.busApi.dto.apiResponse.BusStopApiResponse;
import com.lime.server.busApi.dto.apiResponse.BusStopRouteApiResponse;
import com.lime.server.busApi.dto.apiResponse.CityApiResponse;
import com.lime.server.busApi.error.BusAPIErrorCode;
import com.lime.server.busApi.error.BusAPIException;
import com.lime.server.busApi.error.BusAPIErrorResponse;
import com.lime.server.tico.dto.response.BusRouteResponse;
import com.lime.server.tico.dto.response.BusStationResponse;
import com.lime.server.tico.dto.response.CityResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class BusApiService {
    private static final String CITY_CODE_API_URL = "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getCtyCodeList";
    private static final String BUS_STOP_API_URL = "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getSttnNoList";
    private static final String BUS_STOP_ROUTE_API_URL = "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getSttnThrghRouteList";
    private static final String ARRIVE_BUS_API_URL = "http://apis.data.go.kr/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList";

    @Value("${api.serviceKey}")
    public String serviceKey;

    public CityResponse getCities() throws IOException, JAXBException {
        URL url = getCitiesUrl();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        int responseCode = conn.getResponseCode();
//        log.info("Response code: " + responseCode); //항상 200으로 들어옴

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();

        ObjectMapper jsonMapper = new ObjectMapper();
//        log.info(sb.toString());

        try {
            CityApiResponse cityCodeApiResponse = jsonMapper.readValue(sb.toString(), CityApiResponse.class);

            return CityResponse.from(cityCodeApiResponse);
        } catch (JsonParseException e) {
            //에러메시지는 xml로 들어옴
            JAXBContext context = JAXBContext.newInstance(BusAPIErrorResponse.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            StringReader reader = new StringReader(sb.toString());
            BusAPIErrorResponse response = (BusAPIErrorResponse) unmarshaller.unmarshal(reader);

            log.info("errorMsg: " + response.getCmmMsgHeader().getErrMsg());
            log.info("returnAuthMsg: " + response.getCmmMsgHeader().getReturnAuthMsg() + " " + response.getCmmMsgHeader().getReturnReasonCode());

            int reasonCode = Integer.parseInt(response.getCmmMsgHeader().getReturnReasonCode());
            throw new BusAPIException(BusAPIErrorCode.getDescription(
                    reasonCode)
            );
        }
    }

    public BusStationResponse getBusStations(String cityCode, int pageNum) throws IOException {
        URL url = getBusStationUrl(cityCode, pageNum);
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
        URL url = getBusRouteURL(cityCode, nodeId);
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

        return BusRouteResponse.from(busStopRouteApiResponse);
    }

    public BusArriveApiResponse getArriveBuses(String cityCode, String nodeId) throws IOException {
        URL url = getArriveBusesURL(cityCode, nodeId);

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

//        System.out.println(sb.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
                true); //빈 문자열이 올 때(도착 예정 버스가 없을 때) 처리
        BusArriveApiResponse busArriveApiResponse = objectMapper.readValue(sb.toString(), BusArriveApiResponse.class);

//        if ("00".equals(busArriveApiResponse.getResponse().getHeader().getResultCode())) {
//            BusArriveApiResponse.Items busArriveApiItems = busArriveApiResponse.getResponse().getBody().getItems();
//
//            if (busArriveApiItems == null) {
//                return new BusArriveApiResponse();
//            }
//
//            List<ArriveBus> cities = busArriveApiItems.getItem();
//            for (ArriveBus bus : cities) {
//                log.info(
//                        "arrprevstationcnt: " + bus.getArrprevstationcnt()
//                                + ", arrtime: " + bus.getArrtime()
//                                + ", nodeid: " + bus.getNodeid()
//                                + ", nodenm: " + bus.getNodenm()
//                                + ", routeid: " + bus.getRouteid()
//                                + ", routeno: " + bus.getRouteno() //버스번호
//                                + ", routetp: " + bus.getRoutetp()
//                                + ", vehicletp: " + bus.getVehicletp()
//                );
//            }
//        }

        return busArriveApiResponse;
    }

    private URL getCitiesUrl() throws MalformedURLException {
        URI uri = UriComponentsBuilder.fromHttpUrl(CITY_CODE_API_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("_type", "json")
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        return uri.toURL();
    }

    private URL getBusStationUrl(String cityCode, int pageNum) throws MalformedURLException {
        URI uri = UriComponentsBuilder.fromHttpUrl(BUS_STOP_API_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("_type", "json")
                .queryParam("numOfRows", "50")
                .queryParam("pageNo", pageNum)
                .queryParam("cityCode", cityCode)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        return uri.toURL();
    }

    private URL getBusRouteURL(String cityCode, String nodeId) throws IOException {
        URI uri = UriComponentsBuilder.fromHttpUrl(BUS_STOP_ROUTE_API_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", "100") //TODO 개선
                .queryParam("_type", "json")
                .queryParam("cityCode", cityCode)
                .queryParam("nodeid", nodeId)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        log.info(uri.toString());

        return uri.toURL();
    }

    private URL getArriveBusesURL(String cityCode, String nodeId) throws IOException {
        URI uri = UriComponentsBuilder.fromHttpUrl(ARRIVE_BUS_API_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("_type", "json")
                .queryParam("cityCode", cityCode)
                .queryParam("nodeId", nodeId)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        log.info(uri.toString());

        return uri.toURL();
    }
}


