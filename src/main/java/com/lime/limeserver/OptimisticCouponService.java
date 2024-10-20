package com.lime.limeserver;

import com.lime.limeserver.entity.OptimisticCoupon;
import com.lime.limeserver.repository.OptimisticCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OptimisticCouponService {
    private final OptimisticCouponRepository optimisticCouponRepository;

    @Transactional
    public Long addCoupon() {
        OptimisticCoupon entity = new OptimisticCoupon();
        optimisticCouponRepository.save(entity);

        return entity.getId();
    }

    @Transactional
    public OptimisticCoupon readCoupon(Long id) {
        OptimisticCoupon optimisticCoupon = optimisticCouponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException());

        return optimisticCoupon;
    }

    @Transactional
    public void increaseCoupon(Long id) {
        OptimisticCoupon optimisticCoupon = optimisticCouponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException());

        optimisticCoupon.addCount(1);
    }
}
