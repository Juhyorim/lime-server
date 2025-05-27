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
import com.lime.server.tico.dto.response.BusRouteResponse;
import com.lime.server.tico.dto.response.BusStationResponse;
import com.lime.server.tico.dto.response.CityResponse;
import java.io.IOException;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class BusApiService {
    private final BusApiClient busApiClient;

    public CityResponse getCities() throws IOException {
        URL citiesUrl = busApiClient.getCitiesUrl();
        StringBuilder sb = busApiClient.getResponse(citiesUrl);

        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            CityApiResponse cityCodeApiResponse = jsonMapper.readValue(sb.toString(), CityApiResponse.class);

            return CityResponse.from(cityCodeApiResponse);
        } catch (JsonParseException e) {
            int reasonCode = busApiClient.getBusApiErrorCode(sb);

            throw new BusAPIException(BusAPIErrorCode.getDescription(
                    reasonCode)
            );
        }
    }

    public BusStationResponse getBusStations(String cityCode, int pageNum, String nodeNm, String nodeNo)
            throws IOException {
        validateCityCode(cityCode);
        validatePageNum(pageNum);

        URL busStationUrl = busApiClient.getBusStationUrl(cityCode, pageNum, nodeNm, nodeNo);
        StringBuilder sb = busApiClient.getResponse(busStationUrl);

        //해당 API는 잘못된 값을 삽입 시 빈 값을 반환함 - JSON PARSE Exception 발생 x
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
                true); //빈 문자열이 올 때(값이 없을 때) 처리
        BusStopApiResponse cityCodeApiResponse = objectMapper.readValue(sb.toString(), BusStopApiResponse.class);

        return BusStationResponse.from(cityCodeApiResponse);
    }

    private void validatePageNum(int pageNum) {
        if (pageNum < 1) {
            throw new IllegalArgumentException("page숫자는 1이상이어야합니다.");
        }
    }

    private void validateCityCode(String cityCode) {
        if (cityCode == null || cityCode.isEmpty()) {
            throw new IllegalArgumentException("도시 코드는 비어있을 수 없습니다.");
        }

        for (int i = 0; i < cityCode.length(); i++) {
            if (!Character.isDigit(cityCode.charAt(i))) {
                throw new IllegalArgumentException("도시 코드는 숫자로만 구성되어야 합니다.");
            }
        }
    }

    //버스정류소에 도착하는 버스목록 조회
    public BusRouteResponse getBusRouteInfo(String cityCode, String nodeId) throws IOException {
        URL busRouteURL = busApiClient.getBusRouteURL(cityCode, nodeId);
        StringBuilder sb = busApiClient.getResponse(busRouteURL);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            BusStopRouteApiResponse busStopRouteApiResponse = objectMapper.readValue(sb.toString(),
                    BusStopRouteApiResponse.class);

            return BusRouteResponse.from(busStopRouteApiResponse);
        } catch (JsonParseException e) {
            int reasonCode = busApiClient.getBusApiErrorCode(sb);

            throw new BusAPIException(BusAPIErrorCode.getDescription(
                    reasonCode)
            );
        }
    }

    public BusArriveApiResponse getArriveBuses(int cityCode, String nodeId) throws IOException {
        URL arriveBusesURL = busApiClient.getArriveBusesURL(cityCode, nodeId);
        log.info("API호출: " + arriveBusesURL.toString());
        StringBuilder sb = busApiClient.getResponse(arriveBusesURL);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
                    true); //빈 문자열이 올 때(도착 예정 버스가 없을 때) 처리
            BusArriveApiResponse busArriveApiResponse = objectMapper.readValue(sb.toString(),
                    BusArriveApiResponse.class);

            return busArriveApiResponse;
        } catch (JsonParseException e) {
            int reasonCode = busApiClient.getBusApiErrorCode(sb);

            throw new BusAPIException(BusAPIErrorCode.getDescription(
                    reasonCode)
            );
        }
    }
}
