package com.lime.server.tico.dto.response;

import com.lime.server.busApi.dto.apiResponse.BusStopRouteApiResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BusRouteResponse {
    private List<BusStation> busStations;

    public static BusRouteResponse from(BusStopRouteApiResponse busStopRouteApiResponse) {
        List<BusStation> cities = new ArrayList<>();

        for (BusStopRouteApiResponse.BusRoute busStop : busStopRouteApiResponse.getResponse().getBody().getItems()
                .getItem()) {
            cities.add(new BusStation(busStop.getRouteid(), busStop.getRouteno(), busStop.getRoutetp(),
                    busStop.getEndnodenm(), busStop.getStartnodenm()));
        }

        return new BusRouteResponse(cities);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class BusStation {
        private String routeId;
        private String routeNo;
        private String routEtp;
        private String endNodeName;
        private String startNodeName;
    }
}
