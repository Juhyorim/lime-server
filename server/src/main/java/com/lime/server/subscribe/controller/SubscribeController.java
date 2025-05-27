package com.lime.server.subscribe.controller;

import com.lime.server.subscribe.dto.BusArriveInfoListResponse;
import com.lime.server.subscribe.dto.BusArriveInfoListResponse.BusArriveInfoResponse;
import com.lime.server.subscribe.entity.BusArriveInfo;
import com.lime.server.subscribe.entity.Subscription;
import com.lime.server.subscribe.dto.SubscribeListResponse;
import com.lime.server.subscribe.dto.SubscribeRequest;
import com.lime.server.subscribe.service.SubscribeService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("${api_prefix}/subscribe")
@RequiredArgsConstructor
@RestController
public class SubscribeController {
    private final SubscribeService subscribeService;

    @Operation(summary = "구독하기")
    @PostMapping
    public ResponseEntity subscribe(@RequestBody SubscribeRequest request) {
        subscribeService.subscribe(request.stationId(), request.routeId(), request.nodeName(), request.nodeNo(),
                request.cityCode(), request.routeNo());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "구독하기 버전2")
    @PostMapping("/version2")
    public ResponseEntity subscribeV2(@RequestBody SubscribeRequest request) {
        subscribeService.subscribeV2(request.stationId(), request.nodeName(), request.nodeNo(),
                request.cityCode());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "구독 정보 조회")
    @GetMapping
    public ResponseEntity<SubscribeListResponse> getList() {
        List<Subscription> subscriptionList = subscribeService.getList();

        return ResponseEntity.ok(SubscribeListResponse.from(subscriptionList));
    }

    @Operation(summary = "구독 버스정보 조회")
    @GetMapping("/busInfo")
    public ResponseEntity<BusArriveInfoListResponse> getBusInfo(@RequestParam int subscriptionId) {
        List<BusArriveInfo> busArriveInfos = subscribeService.getBusInfo(subscriptionId);

        return ResponseEntity.ok(new BusArriveInfoListResponse(BusArriveInfoResponse.from(busArriveInfos)));
    }

    @Operation(summary = "버스정보 조회")
    @GetMapping("/busInfo/version2")
    public ResponseEntity<BusArriveInfoListResponse> getBusInfo(@RequestParam int cityCode,
                                                                @RequestParam String nodeId,
                                                                @RequestParam String routeId) {
        List<BusArriveInfo> busArriveInfos = subscribeService.getBusInfo(cityCode, nodeId, routeId);

        return ResponseEntity.ok(new BusArriveInfoListResponse(BusArriveInfoResponse.from(busArriveInfos)));
    }
}
