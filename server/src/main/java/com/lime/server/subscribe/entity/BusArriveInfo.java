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
@Document(collection = "bus_arrive_info")
public class BusArriveInfo {
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

    public static BusArriveInfo of(LocalDateTime arriveTime, int cityCode, String nodeId, String nodeNo, String nodeName, String routeId, String routeNo, int remainTime, LocalDateTime current) {
        BusArriveInfo info = new BusArriveInfo();
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
}
