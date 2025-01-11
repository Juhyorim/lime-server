package com.lime.server.tico.controller;

import com.lime.server.tico.dto.response.CityResponse;
import com.lime.server.busApi.service.BusApiService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class TicoController {
    private final BusApiService busApiService;

    @GetMapping("/city")
    public ResponseEntity<CityResponse> getCities() throws IOException {
        CityResponse cityResponse = busApiService.getCities();

        return ResponseEntity.ok(cityResponse);
    }
}
