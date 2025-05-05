package com.lime.server.busApi.dto.apiResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BusArriveApiResponse {
    private Response response;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Header header;
        private Body body;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Body {
        private Items items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Items {
        //하나만 오는 경우 단일 객체로 오는 것 처리
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        private List<ArriveBus> item;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArriveBus {
        private int arrprevstationcnt; //도착예정버스 남은 정류장 수
        private int arrtime; //도착예정시간[초]
        private String nodeid; //정류소ID
        private String nodenm; //정류소 이름
        private String routeid; //노선 ID
        private String routeno; //선택적, 303-1
        private String routetp; //노선 유형 (일반버스, 좌석버스)
        private String vehicletp; //도착예정버스 차량 유형 (저상버스, 일반차량)
    }
}
