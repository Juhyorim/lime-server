package com.lime.server.subscribe.repository;

import com.lime.server.subscribe.entity.BusArriveInfo;
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
public class BusArriveInfoCustomRepository {
    private final MongoTemplate mongoTemplate;

    public List<BusArriveInfo> findByCityCodeAndNodeIdAndRouteIdAndDate(int cityCode, String nodeId, String routeId,
                                                                        LocalDate localDate) {

        //문자열 비교로 일단 구현
        // 날짜를 문자열 형태로 변환 (YYYY-MM-DD)
//        String targetDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        log.info("@@: " + targetDate);
//
//        // 정규식을 사용하여 날짜 부분만 매칭
//        String queryString = String.format(
//                "{ " +
//                        "  'cityCode': %d, " +
//                        "  'nodeId': '%s', " +
//                        "  'routeId': '%s', " +
//                        "  'arriveTime': { '$regex': '^%sT' } " +
//                        "}",
//                cityCode, nodeId, routeId, targetDate
//        );
//
//        Query query = new BasicQuery(queryString);
//        return mongoTemplate.find(query, BusArriveInfo.class);

        //안돌아가는 로직 -> 29일과 30일 비교안됨
        LocalDateTime startOfDay = localDate.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = localDate.plusDays(1).atStartOfDay();
//
//        // MongoDB native query 직접 작성
//        String queryString = String.format(
//                "{ 'cityCode': %d, 'nodeId': '%s', 'routeId': '%s', 'arriveTime': {'$gte': '%s', '$lt': '%s'}}",
//                cityCode, nodeId, routeId, startOfDay, endOfDay
//        );
//
//        Query query = new BasicQuery(queryString);
//
//        return mongoTemplate.find(query, BusArriveInfo.class);

        //@TODO mongodb 시간 비교 관련 비교 공부하기
        Query query = new Query();
        query.addCriteria(Criteria.where("cityCode").is(cityCode)
                .and("nodeId").is(nodeId)
                .and("routeId").is(routeId)
                .and("arriveTime").gte(startOfDay).lt(endOfDay));

        //쌩 쿼리:   { "cityCode" : 37050, "nodeId" : "GMB4", "routeId" : "GMB9120", "arriveTime" : { "$gte" : "2025-05-27T00:00", "$lt" : "2025-05-28T00:00"}} fields: Document{{}} sort: { "cityCode" : 37050, "nodeId" : "GMB4", "routeId" : "GMB9120", "arriveTime" : { "$gte" : "2025-05-27T00:00", "$lt" : "2025-05-28T00:00"}}
        //Criteria: { "cityCode" : 37050, "nodeId" : "GMB4", "routeId" : "GMB9120", "arriveTime" : { "$gte" : { "$date" : "2025-05-26T15:00:00Z"}, "$lt" : { "$date" : "2025-05-27T15:00:00Z"}}} fields: Document{{}} sort: { "cityCode" : 37050, "nodeId" : "GMB4", "routeId" : "GMB9120", "arriveTime" : { "$gte" : { "$java" : 2025-05-27T00:00 }, "$lt" : { "$java" : 2025-05-28T00:00 } } }
        return mongoTemplate.find(query, BusArriveInfo.class);
    }
}
