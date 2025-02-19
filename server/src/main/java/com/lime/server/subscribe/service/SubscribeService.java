package com.lime.server.subscribe.service;

public interface SubscribeService {
    void subscribe(String stationId, String routeId, String nodeName, String nodeNo, int cityCode);
}