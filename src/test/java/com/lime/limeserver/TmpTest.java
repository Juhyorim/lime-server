package com.lime.limeserver;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TmpTest {
    @Autowired
    private OptimisticCouponService optimisticCouponService;

    @Test
    void simpleTest() {
//        Long aLong = optimisticCouponService.addCoupon();
//        optimisticCouponService.increaseCoupon(1L);

        optimisticCouponService.readCoupon(1L);
    }

    @Test
    void 동시테스트() throws InterruptedException {
        final int executorNumber = 20;
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        final CountDownLatch countDownLatch = new CountDownLatch(executorNumber);

        final AtomicInteger successCount = new AtomicInteger();
        final AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < executorNumber; i++) {
            executorService.execute(() -> {
                try {
                    optimisticCouponService.increaseCoupon(1L);
                    successCount.getAndIncrement();
                    System.out.println("쿠폰 발급");
                } catch (Exception e) {
                    failCount.getAndIncrement();
                    e.printStackTrace();
                }

                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        System.out.println("발급된 쿠폰의 개수 = " + successCount.get());
        System.out.println("실패 횟수 = " + failCount.get());

        assertEquals(failCount.get() + successCount.get(), executorNumber);
    }
}
