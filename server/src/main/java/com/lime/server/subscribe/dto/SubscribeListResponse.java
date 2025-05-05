package com.lime.server.subscribe.dto;

import com.lime.server.subscribe.entity.Subscription;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeListResponse {
    private List<SubscribeResponse> subscribeResponseList;

    public static SubscribeListResponse from(List<Subscription> subscriptions) {
        List<SubscribeResponse> subscribeResponses = new ArrayList<>();
        for (Subscription subscription : subscriptions) {
            subscribeResponses.add(SubscribeResponse.from(subscription));
        }

        return new SubscribeListResponse(subscribeResponses);
    }
}
