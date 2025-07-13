package com.lime.server.subscribe.service;

import com.lime.server.auth.entity.Member;
import com.lime.server.subscribe.entity.ArrangedBusArriveInfo;
import com.lime.server.subscribe.entity.BusArriveInfo;
import com.lime.server.subscribe.entity.Subscription;
import java.time.LocalDate;
import java.util.List;

public interface SubscribeService {
    void subscribeV2(Member member, String stationId, String nodeName, String nodeNo, int cityCode);

    List<Subscription> getList(Member member);

    List<BusArriveInfo> getBusInfo(Member member, Integer subscriptionId);

    List<BusArriveInfo> getBusInfo(int cityCode, String nodeId, String routeId);

    List<BusArriveInfo> getBusInfoYesterday(int cityCode, String nodeId);

    List<BusArriveInfo> getBusInfoWithDate2(int cityCode, String nodeId, LocalDate localDate);

    //도시코드, 정류장, 버스로 특정날짜 도착시간 찾기
    List<BusArriveInfo> getBusInfoWithDate(int cityCode, String nodeId, String routeId, LocalDate localDate);

    List<ArrangedBusArriveInfo> getArrangedBusInfoWithDate(int cityCode, String nodeId, String routeId,
                                                           LocalDate localDate);

    List<ArrangedBusArriveInfo> getAllBusInfoWithDate(int cityCode, String nodeId, LocalDate localDate);

    void cancel(Member member, Integer subscribeId);
}