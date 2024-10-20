package com.lime.limeserver;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomRetryListener implements RetryListener {
    private static final Logger logger = LoggerFactory.getLogger(CustomRetryListener.class);

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        return true; // 재시도 프로세스를 시작할지 여부
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        // 재시도 완료 후 호출됨
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
//        logger.info("재시도 발생. 시도 횟수: {}", context.getRetryCount());
    }
}