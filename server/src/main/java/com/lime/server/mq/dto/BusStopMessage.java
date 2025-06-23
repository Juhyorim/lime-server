package com.lime.server.mq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BusStopMessage {
    private int cityCode;
    private String nodeId;
    private String nodeNo;

    public static BusStopMessage of(int cityCode, String nodeId, String nodeNo) {
        return new BusStopMessage(cityCode, nodeId, nodeNo);
    }
}
