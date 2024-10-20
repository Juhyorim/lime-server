package com.lime.limeserver;

import com.lime.limeserver.entity.OptimisticCoupon;
import com.lime.limeserver.repository.OptimisticCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 3, //최대 재시도 횟수
            backoff = @Backoff(100) // 재시도 사이 시간 설정 (재시도 사이 0.1초 대기)
//            listeners = "customRetryListener" //로그찍기용 리스너
    )
    public void increaseCoupon(Long id) {
        OptimisticCoupon optimisticCoupon = optimisticCouponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException());

        optimisticCoupon.addCount(1);
    }

    @Recover
    public void recoverIncreaseCoupon(ObjectOptimisticLockingFailureException e, Long id) {
        // 재시도 실패 후 실행될 메서드
        log.warn("낙관적 락킹 실패로 인한 복구 프로세스 시작. Coupon ID: {}", id);
//        performAlternativeAction(id);
    }
}
