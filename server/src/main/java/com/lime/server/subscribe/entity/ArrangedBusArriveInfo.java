package com.lime.server.subscribe.entity;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "arranged_bus_arrive_info")
public class ArrangedBusArriveInfo { //정리된 버스정보
    @Id
    private String id;

    private LocalDateTime arriveTime;

    private int cityCode;
    private String nodeId;
    private String nodeNo;
    private String nodeName;
    private String routeId;
    private String routeNo;

    private LocalDateTime createdAt;
    private int remainTime;

    public static ArrangedBusArriveInfo of(LocalDateTime arriveTime, int cityCode, String nodeId, String nodeNo,
                                           String nodeName, String routeId, String routeNo, int remainTime,
                                           LocalDateTime current) {
        ArrangedBusArriveInfo info = new ArrangedBusArriveInfo();
        info.arriveTime = arriveTime;
        info.cityCode = cityCode;
        info.nodeId = nodeId;
        info.nodeNo = nodeNo;
        info.nodeName = nodeName;
        info.routeId = routeId;
        info.routeNo = routeNo;
        info.remainTime = remainTime;
        info.createdAt = current;

        return info;
    }

    public static ArrangedBusArriveInfo from(BusArriveInfo info) {
        return ArrangedBusArriveInfo.of(info.getArriveTime(), info.getCityCode(), info.getNodeId(), info.getNodeNo(),
                info.getNodeName(), info.getRouteId(), info.getRouteNo(), info.getRemainTime(), info.getCreatedAt());
    }
}
