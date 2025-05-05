package com.lime.server.subscribe.repository;

import com.lime.server.subscribe.entity.BusArriveInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusArriveInfoRepository extends JpaRepository<BusArriveInfo, Integer> {
    List<BusArriveInfo> findByCityCodeAndNodeIdAndRouteId(int cityCode, String nodeId, String routeId);
}
