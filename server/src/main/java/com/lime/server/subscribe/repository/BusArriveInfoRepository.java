package com.lime.server.subscribe.repository;

import com.lime.server.subscribe.entity.BusArriveInfo;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BusArriveInfoRepository extends MongoRepository<BusArriveInfo, Integer> {
    List<BusArriveInfo> findByCityCodeAndNodeIdAndRouteId(int cityCode, String nodeId, String routeId);
    List<BusArriveInfo> findByCityCodeAndNodeId(int cityCode, String nodeId);
}
