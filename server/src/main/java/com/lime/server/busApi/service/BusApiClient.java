package com.lime.server.busApi.service;

import com.lime.server.busApi.error.BusAPIErrorCode;
import com.lime.server.busApi.error.BusAPIErrorResponse;
import com.lime.server.busApi.error.BusAPIException;
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
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class BusApiClient {
    private static final String CITY_CODE_API_URL = "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getCtyCodeList";
    private static final String BUS_STOP_API_URL = "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getSttnNoList";
    private static final String BUS_STOP_ROUTE_API_URL = "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getSttnThrghRouteList";
    private static final String ARRIVE_BUS_API_URL = "http://apis.data.go.kr/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList";

    @Value("${api.serviceKey}")
    public String serviceKey;

    public int getBusApiErrorCode(StringBuilder sb) {
        //에러메시지는 xml로 들어옴
        try {
            JAXBContext context = JAXBContext.newInstance(BusAPIErrorResponse.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            StringReader reader = new StringReader(sb.toString());
            BusAPIErrorResponse response = (BusAPIErrorResponse) unmarshaller.unmarshal(reader);

            log.warn("errorMsg: " + response.getCmmMsgHeader().getErrMsg());
            log.warn(
                    "returnAuthMsg: " + response.getCmmMsgHeader().getReturnAuthMsg() + " " + response.getCmmMsgHeader()
                            .getReturnReasonCode());

            return Integer.parseInt(response.getCmmMsgHeader().getReturnReasonCode());
        } catch (JAXBException e) {
            log.warn("BusApiService - getBusApiErrorCode - 에러메시지 xml 파싱 실패");

            throw new BusAPIException(BusAPIErrorCode.getDescription(BusAPIErrorCode.UNKNOWN_ERROR));
        }
    }

    public StringBuilder getCitiesResponse() throws IOException {
        URL citiesUrl = getCitiesUrl();

        return getResponse(citiesUrl);
    }

    public StringBuilder getBusStationsResponse(String cityCode, int pageNum, String nodeNm, String nodeNo) throws IOException {
        URL busStationUrl = getBusStationUrl(cityCode, pageNum, nodeNm, nodeNo);

        return getResponse(busStationUrl);
    }

    public StringBuilder getBusRouteInfoResponse(String cityCode, String nodeId) throws IOException {
        URL busRouteURL = getBusRouteURL(cityCode, nodeId);

        return getResponse(busRouteURL);
    }

    public StringBuilder getArriveBusesResponse(int cityCode, String nodeId) throws IOException {
        URL arriveBusesURL = getArriveBusesURL(cityCode, nodeId);

        return getResponse(arriveBusesURL);
    }

    private StringBuilder getResponse(URL url) {
        HttpURLConnection conn = null;
        BufferedReader rd = null;
        StringBuilder sb = new StringBuilder();

        try {
            conn = getHttpURLConnection(url);
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            return sb;
        } catch (IOException e) {
            log.warn("BusApiService - getResponse - fetching data 에러발생");
            log.warn(e.getMessage());

            throw new RuntimeException("API를 불러올 수 없습니다");
        } finally {
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    log.warn("BusApiService - getResponse - BufferedReader Close 과정에서 에러발생");
                    log.warn(e.getMessage());
                }
            }

            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        return conn;
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

    private URL getBusStationUrl(String cityCode, int pageNum, String nodeNm, String nodeNo)
            throws MalformedURLException {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(BUS_STOP_API_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("_type", "json")
                .queryParam("numOfRows", "50")
                .queryParam("pageNo", pageNum)
                .queryParam("cityCode", cityCode);

        if (nodeNm != null) {
            uriBuilder = uriBuilder
                    .queryParam("nodeNm", nodeNm);
        } else if (nodeNo != null) {
            uriBuilder = uriBuilder
                    .queryParam("nodeNo", nodeNo);
        }

        return uriBuilder
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri().toURL();
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

        return uri.toURL();
    }

    private URL getArriveBusesURL(int cityCode, String nodeId) throws IOException {
        URI uri = UriComponentsBuilder.fromHttpUrl(ARRIVE_BUS_API_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("_type", "json")
                .queryParam("cityCode", cityCode)
                .queryParam("nodeId", nodeId)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        return uri.toURL();
    }
}
