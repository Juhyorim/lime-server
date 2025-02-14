package com.lime.server.tico.controller;

import com.lime.server.subscribe.dto.SubscribeRequest;
import com.lime.server.subscribe.service.SubscribeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SubscribeController {
    private final SubscribeService subscribeService;

    @Operation(summary = "구독하기")
    @PostMapping("/subscribe")
    public ResponseEntity subscribe(@RequestBody SubscribeRequest request) {
        subscribeService.subscribe(request.stationId(), request.routeId());

        return null;
    }
}
