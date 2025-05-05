package com.lime.server.tico.controller;

import com.lime.server.tico.dto.response.BusRouteResponse;
import com.lime.server.tico.dto.response.BusStationResponse;
import com.lime.server.tico.dto.response.CityResponse;
import com.lime.server.busApi.service.BusApiService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("${api_prefix}/tico")
@RequiredArgsConstructor
@RestController
public class TicoController {
    private final BusApiService busApiService;

    //memo: 구미시 citycode = 37050
    //거상빌딩 nodeId = GMB4, nodeNo = 10004

    @Operation(summary = "테스트")
    @PostMapping("/test")
    public ResponseEntity<String> corsTest() {
        return ResponseEntity.ok("cors good!~~");
    }

    @Operation(summary = "도시 목록 조회")
    @GetMapping("/city")
    public ResponseEntity<CityResponse> getCities() throws IOException, JAXBException {
        CityResponse cityResponse = busApiService.getCities();

        return ResponseEntity.ok(cityResponse);
    }

    @Operation(summary = "citycode 기반, 버스 정류소 조회")
    @GetMapping("/bus-station/{cityCode}/{pageNum}")
    public ResponseEntity<BusStationResponse> getBusStations(@PathVariable(name = "cityCode") String cityCode,
                                                             @PathVariable(name = "pageNum") int pageNum)
            throws IOException {
        BusStationResponse busStations = busApiService.getBusStations(cityCode, pageNum);

        return ResponseEntity.ok(busStations);
    }

    @GetMapping("/bus-route/{cityCode}/{nodeId}")
    public ResponseEntity<BusRouteResponse> getRoutes(@PathVariable(name = "cityCode") String cityCode,
                                                      @PathVariable(name = "nodeId") String nodeId)
            throws IOException {
        BusRouteResponse busRouteInfo = busApiService.getBusRouteInfo(cityCode, nodeId);

        return ResponseEntity.ok(busRouteInfo);
    }
}
