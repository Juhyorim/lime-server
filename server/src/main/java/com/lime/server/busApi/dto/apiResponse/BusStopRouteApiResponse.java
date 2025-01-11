package com.lime.server.busApi.dto.apiResponse;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BusStopRouteApiResponse {
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
        private List<BusRoute> item;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusRoute {
        private String routeid;
        private String routeno;
        private String routetp;
        private String endnodenm;
        private String startnodenm;
    }
}
