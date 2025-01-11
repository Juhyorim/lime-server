package com.lime.server.bus.dto.apiResponse;

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
        private List<ArriveBus> item;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArriveBus {
        private int arrprevstationcnt;
        private int arrtime;
        private String nodeid;
        private String nodenm;
        private String routeid;
        private String routeno; //선택적, 303-1
        private String routetp;
        private String vehicletp;
    }
}
