package com.lime.server.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;

@Component
public class KoreanTimeUtil {
    private static final LocalTime NIGHT_START = LocalTime.of(23, 0);
    private static final LocalTime MORNING_END = LocalTime.of(6, 0);

    public LocalTime getCurrentTime() {
        ZonedDateTime koreaTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        return koreaTime.toLocalTime();
    }

    public LocalDateTime getCurrentDateTime() {
        ZonedDateTime koreaTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        return koreaTime.toLocalDateTime();
    }

    public boolean isNightTime() {
        ZonedDateTime koreaTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalTime currentTime = koreaTime.toLocalTime();

        return currentTime.isAfter(NIGHT_START) || currentTime.isBefore(MORNING_END);
    }
}
