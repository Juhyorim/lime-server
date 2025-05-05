package com.lime.server.busApi.error;

public enum BusAPIErrorCode {
    APPLICATION_ERROR(1, "어플리케이션 에러"),
    HTTP_ERROR(4, "HTTP 에러"),
    NO_OPENAPI_SERVICE_ERROR(12, "해당 오픈 API 서비스가 없거나 폐기됨"),
    SERVICE_ACCESS_DENIED_ERROR(20, "서비스 접근 거부"),
    LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR(22, "서비스 요청제한횟수 초과 에러"),
    SERVICE_KEY_IS_NOT_REGISTERED_ERROR(30, "등록되지 않은 서비스키"),
    DEADLINE_HAS_EXPIRED_ERROR(31, "사용기간 만료"),
    UNREGISTERED_IP_ERROR(32, "등록되지 않은 IP"),
    UNKNOWN_ERROR(99, "기타 에러");

    final int code;
    final String description;

    BusAPIErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String getDescription(int code) {
        for (BusAPIErrorCode errorCode : BusAPIErrorCode.values()) {
            if (errorCode.code == code) {
                return errorCode.description;
            }
        }

        return "알 수 없는 에러";
    }
}
