package com.lime.limeserver.repository;

import com.lime.limeserver.entity.OptimisticCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptimisticCouponRepository extends JpaRepository<OptimisticCoupon, Long> {
}
