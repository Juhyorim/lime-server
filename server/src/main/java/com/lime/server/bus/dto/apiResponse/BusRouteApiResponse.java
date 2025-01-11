package com.lime.server.bus.dto.apiResponse;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BusRouteApiResponse {
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
        private List<BusStation> item;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusStation {
        private String routeid;
        private String nodeid;
        private String nodenm;
        private String nodeno; //선택적, 정류소 번호
        private int nodeord;
        private double gpslati;
        private double gpslong;
        private int updowncd; //선택적
    }
}
