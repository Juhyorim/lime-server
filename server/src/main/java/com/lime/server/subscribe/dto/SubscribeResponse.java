package com.lime.server.subscribe.dto;

import com.lime.server.subscribe.entity.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SubscribeResponse {
    private Integer id;
    private int cityCode;
    private String nodeId;
    private String nodeNo;
    private String nodeName;
    private String routeId;

    public static SubscribeResponse from(Subscription subscription) {
        return new SubscribeResponse(subscription.getId(), subscription.getCityCode(), subscription.getNodeId(),
                subscription.getNodeNo(), subscription.getNodeName(), subscription.getRouteId());
    }
}
