package com.lime.server.error;

import com.lime.server.busApi.error.BusAPIException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
    static final String BUS_API_EXCEPTION_MESSAGE = "API 에러 발생";

    @ExceptionHandler(BusAPIException.class)
    public ResponseEntity<ErrorDto> bindBusAPIException(final BusAPIException e) {
        return ResponseEntity.internalServerError().body(new ErrorDto(BUS_API_EXCEPTION_MESSAGE));
    }
}
