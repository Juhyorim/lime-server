package com.lime.server.subscribe.controller;

import com.lime.server.auth.entity.Member;
import com.lime.server.subscribe.dto.BusArriveInfoListResponse;
import com.lime.server.subscribe.dto.BusArriveInfoListResponse.BusArriveInfoResponse;
import com.lime.server.subscribe.entity.ArrangedBusArriveInfo;
import com.lime.server.subscribe.entity.BusArriveInfo;
import com.lime.server.subscribe.entity.Subscription;
import com.lime.server.subscribe.dto.SubscribeListResponse;
import com.lime.server.subscribe.dto.SubscribeRequest;
import com.lime.server.subscribe.service.SubscribeService;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Operation(summary = "구독 취소")
    @DeleteMapping("/{subscribeId}")
    public ResponseEntity subscribe(@AuthenticationPrincipal Member member, @PathVariable(name = "subscribeId") Integer subscribeId) {
        subscribeService.cancel(member, subscribeId);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "구독하기 버전2")
    @PostMapping("/version2")
    public ResponseEntity subscribeV2(@AuthenticationPrincipal Member member, @RequestBody SubscribeRequest request) {
        subscribeService.subscribeV2(member, request.stationId(), request.nodeName(), request.nodeNo(),
                request.cityCode());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "구독 정보 조회")
    @GetMapping
    public ResponseEntity<SubscribeListResponse> getList(@AuthenticationPrincipal Member member) {
        List<Subscription> subscriptionList = subscribeService.getList(member);

        return ResponseEntity.ok(SubscribeListResponse.from(subscriptionList));
    }

    @Operation(summary = "구독 버스도착 정보 조회")
    @GetMapping("/busInfo")
    public ResponseEntity<BusArriveInfoListResponse> getBusInfo(@AuthenticationPrincipal Member member, @RequestParam int subscriptionId) {
        List<BusArriveInfo> busArriveInfos = subscribeService.getBusInfo(member, subscriptionId);

        return ResponseEntity.ok(new BusArriveInfoListResponse(BusArriveInfoResponse.from(busArriveInfos)));
    }

    @Operation(summary = "[가공됨] 특정날짜 버스도착 정보 조회")
    @GetMapping("/busInfo/version3")
    public ResponseEntity<BusArriveInfoListResponse> getBusInfoWithDate(@RequestParam int cityCode,
                                                                        @RequestParam String nodeId,
                                                                        @RequestParam String routeId,
                                                                        @RequestParam LocalDate localDate) {
        List<ArrangedBusArriveInfo> busArriveInfos = subscribeService.getArrangedBusInfoWithDate(cityCode, nodeId, routeId, localDate);

        return ResponseEntity.ok(new BusArriveInfoListResponse(BusArriveInfoResponse.fromArranged(busArriveInfos)));
    }

    @Operation(summary = "[가공됨, 전체] 특정날짜 전체 버스도착 정보 조회")
    @GetMapping("/busInfo/version4")
    public ResponseEntity<BusArriveInfoListResponse> getAllBusInfoWithDate(@RequestParam int cityCode,
                                                                        @RequestParam String nodeId,
                                                                        @RequestParam LocalDate localDate) {
        List<ArrangedBusArriveInfo> busArriveInfos = subscribeService.getAllBusInfoWithDate(cityCode, nodeId, localDate);

        return ResponseEntity.ok(new BusArriveInfoListResponse(BusArriveInfoResponse.fromArranged(busArriveInfos)));
    }

    @Operation(summary = "[가공안됨] 특정날짜 버스도착 정보 조회")
    @GetMapping("/busInfo/version5")
    public ResponseEntity<BusArriveInfoListResponse> getArrangedBusInfoWithDate(@RequestParam int cityCode,
                                                                        @RequestParam String nodeId,
                                                                        @RequestParam String routeId,
                                                                        @RequestParam LocalDate localDate) {
        List<BusArriveInfo> busArriveInfos = subscribeService.getBusInfoWithDate(cityCode, nodeId, routeId, localDate);

        return ResponseEntity.ok(new BusArriveInfoListResponse(BusArriveInfoResponse.from(busArriveInfos)));
    }
}
