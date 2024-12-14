package com.lime.server.bus.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CityApiResponse {
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
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Items {
        private List<City> item;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class City {
        private int citycode;
        private String cityname;
    }
}
