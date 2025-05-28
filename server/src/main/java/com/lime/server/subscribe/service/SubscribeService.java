package com.lime.server.subscribe.service;

import com.lime.server.auth.entity.Member;
import com.lime.server.subscribe.entity.BusArriveInfo;
import com.lime.server.subscribe.entity.Subscription;
import java.io.IOException;
import java.util.List;

public interface SubscribeService {
    void subscribe(Member member, String stationId, String routeId, String nodeName, String nodeNo, int cityCode, String routeNo);

    void subscribeV2(Member member, String stationId, String nodeName, String nodeNo, int cityCode);

    List<Subscription> getList(Member member);

    void getSubscribedBusInfo() throws IOException;

    List<BusArriveInfo> getBusInfo(Member member, Integer subscriptionId);

    List<BusArriveInfo> getBusInfo(int cityCode, String nodeId, String routeId);

    void cancel(Member member, Integer subscribeId);
}