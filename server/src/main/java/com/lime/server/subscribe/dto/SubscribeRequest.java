package com.lime.server.subscribe.dto;

public record SubscribeRequest(String stationId, String routeId, String nodeName, String nodeNo, int cityCode ,String routeNo) {
}
