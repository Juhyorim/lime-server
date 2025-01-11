package com.lime.server.tico.dto.response;

import com.lime.server.busApi.dto.apiResponse.CityApiResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CityResponse {
    private List<CityInfo> cities;

    public static CityResponse from(CityApiResponse cityCodeApiResponse) {
        List<CityInfo> cities = new ArrayList<>();

        for (CityApiResponse.City city : cityCodeApiResponse.getResponse().getBody().getItems().getItem()) {
            cities.add(new CityInfo(city.getCitycode(), city.getCityname()));
        }

        return new CityResponse(cities);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CityInfo {
        private int cityCode;
        private String cityName;
    }
}
