package com.lime.server.subscribe.service;

import com.lime.server.subscribe.Subscription;
import java.util.List;

public interface SubscribeService {
    void subscribe(String stationId, String routeId, String nodeName, String nodeNo, int cityCode);

    List<Subscription> getList();
}