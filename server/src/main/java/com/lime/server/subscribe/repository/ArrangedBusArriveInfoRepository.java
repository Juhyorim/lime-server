package com.lime.server.subscribe.repository;

import com.lime.server.subscribe.entity.ArrangedBusArriveInfo;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArrangedBusArriveInfoRepository extends MongoRepository<ArrangedBusArriveInfo, Integer> {
    List<ArrangedBusArriveInfo> findByCityCodeAndNodeIdAndRouteId(int cityCode, String nodeId, String routeId);
    List<ArrangedBusArriveInfo> findByCityCodeAndNodeId(int cityCode, String nodeId);
}
