package com.lime.server.subscribe.repository;

import com.lime.server.subscribe.entity.ArrangedBusArriveInfo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ArrangedBusArriveInfoCustomRepository {
    private final MongoTemplate mongoTemplate;

    public List<ArrangedBusArriveInfo> findByCityCodeAndNodeIdAndRouteIdAndDate(int cityCode, String nodeId, String routeId,
                                                                                LocalDate localDate) {
        //안돌아가는 로직 -> 29일과 30일 비교안됨
        LocalDateTime startOfDay = localDate.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = localDate.plusDays(1).atStartOfDay();

        Query query = new Query();
        query.addCriteria(Criteria.where("cityCode").is(cityCode)
                .and("nodeId").is(nodeId)
                .and("routeId").is(routeId)
                .and("arriveTime").gte(startOfDay).lt(endOfDay));

        return mongoTemplate.find(query, ArrangedBusArriveInfo.class);
    }

    public List<ArrangedBusArriveInfo> findByCityCodeAndNodeIdAndDate(int cityCode, String nodeId, LocalDate localDate) {
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.plusDays(1).atStartOfDay();

        //@TODO mongodb 시간 비교 관련 비교 공부하기
        Query query = new Query();
        query.addCriteria(Criteria.where("cityCode").is(cityCode)
                .and("nodeId").is(nodeId)
                .and("arriveTime").gte(startOfDay).lt(endOfDay));

        return mongoTemplate.find(query, ArrangedBusArriveInfo.class);
    }
}
