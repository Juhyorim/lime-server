package com.lime.server.tico.dto.response;

import com.lime.server.busApi.dto.apiResponse.BusStopApiResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BusStationResponse {
    private List<BusStation> busStations;

    public static BusStationResponse from(BusStopApiResponse busStopApiResponse) {
        List<BusStation> cities = new ArrayList<>();

        for (BusStopApiResponse.BusStop busStop : busStopApiResponse.getResponse().getBody().getItems().getItem()) {
            cities.add(new BusStation(busStop.getNodeid(), busStop.getNodenm(), busStop.getNodeno()));
        }

        return new BusStationResponse(cities);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class BusStation {
        private String nodeId;
        private String nodeName;
        private int nodeNo;
    }
}
