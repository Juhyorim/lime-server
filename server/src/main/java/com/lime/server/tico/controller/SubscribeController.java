package com.lime.server.tico.controller;

import com.lime.server.subscribe.Subscription;
import com.lime.server.subscribe.dto.SubscribeListResponse;
import com.lime.server.subscribe.dto.SubscribeRequest;
import com.lime.server.subscribe.service.SubscribeService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("${api_prefix}/subscribe")
@RequiredArgsConstructor
@RestController
public class SubscribeController {
    private final SubscribeService subscribeService;

    @Operation(summary = "구독하기")
    @PostMapping
    public ResponseEntity subscribe(@RequestBody SubscribeRequest request) {
        subscribeService.subscribe(request.stationId(), request.routeId(), request.nodeName(), request.nodeNo(),
                request.cityCode());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "구독 정보 조회")
    @GetMapping
    public ResponseEntity<SubscribeListResponse> getList() {
        List<Subscription> subscriptionList = subscribeService.getList();

        return ResponseEntity.ok(SubscribeListResponse.from(subscriptionList));
    }
}
