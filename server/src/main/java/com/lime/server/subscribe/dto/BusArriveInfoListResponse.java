package com.lime.server.subscribe.dto;

import com.lime.server.subscribe.entity.ArrangedBusArriveInfo;
import com.lime.server.subscribe.entity.BusArriveInfo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BusArriveInfoListResponse {
    private List<BusArriveInfoResponse> response;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class BusArriveInfoResponse {
        private String nodeId;
        private String nodeName;
        private String routeId;
        private String routeNo;
        private LocalDateTime arriveTime;
        private int remainTime;

        public static List<BusArriveInfoResponse> from(List<BusArriveInfo> busArriveInfos) {
            List<BusArriveInfoResponse> response = new ArrayList<>();
            for (BusArriveInfo busArriveInfo : busArriveInfos) {
                response.add(new BusArriveInfoResponse(busArriveInfo.getNodeId(), busArriveInfo.getNodeName(), busArriveInfo.getRouteId(), busArriveInfo.getRouteNo(),busArriveInfo.getArriveTime(), busArriveInfo.getRemainTime()));
            }

            return response;
        }

        public static List<BusArriveInfoResponse> fromArranged(List<ArrangedBusArriveInfo> busArriveInfos) {
            List<BusArriveInfoResponse> response = new ArrayList<>();
            for (ArrangedBusArriveInfo busArriveInfo : busArriveInfos) {
                response.add(new BusArriveInfoResponse(busArriveInfo.getNodeId(), busArriveInfo.getNodeName(), busArriveInfo.getRouteId(), busArriveInfo.getRouteNo(),busArriveInfo.getArriveTime(), busArriveInfo.getRemainTime()));
            }

            return response;
        }
    }
}
