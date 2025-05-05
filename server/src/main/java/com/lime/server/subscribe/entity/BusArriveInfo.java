package com.lime.server.subscribe.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BusArriveInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private LocalDateTime arriveTime;

    private int cityCode;
    private String nodeId;
    private String nodeNo;
    private String nodeName;
    private String routeId;

    private LocalDateTime createdAt = LocalDateTime.now();
    private int remainTime;

    public static BusArriveInfo of(LocalDateTime arriveTime, int cityCode, String nodeId, String nodeNo, String nodeName, String routeId, int remainTime) {
        BusArriveInfo info = new BusArriveInfo();
        info.arriveTime = arriveTime;
        info.cityCode = cityCode;
        info.nodeId = nodeId;
        info.nodeNo = nodeNo;
        info.nodeName = nodeName;
        info.routeId = routeId;
        info.remainTime = remainTime;

        return info;
    }
}
