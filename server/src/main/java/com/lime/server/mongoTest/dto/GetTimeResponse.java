package com.lime.server.mongoTest.dto;

import java.time.LocalDateTime;

public record GetTimeResponse(String entityId, LocalDateTime time, String timeString) {
}
