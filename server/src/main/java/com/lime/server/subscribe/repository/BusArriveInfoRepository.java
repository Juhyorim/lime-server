package com.lime.server.subscribe.repository;

import com.lime.server.subscribe.BusArriveInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusArriveInfoRepository extends JpaRepository<BusArriveInfo, Integer> {
}
