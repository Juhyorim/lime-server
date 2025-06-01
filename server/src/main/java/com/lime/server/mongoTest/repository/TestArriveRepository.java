package com.lime.server.mongoTest.repository;

import com.lime.server.mongoTest.TestArriveInfo.TestArriveInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestArriveRepository extends MongoRepository<TestArriveInfo, String> {

}
