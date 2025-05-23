package com.lime.server.error;

import com.lime.server.busApi.error.BusAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
    static final String BUS_API_EXCEPTION_MESSAGE = "API 에러 발생";

    @ExceptionHandler(BusAPIException.class)
    public ResponseEntity<ErrorDto> bindBusAPIException(final BusAPIException e) {
        return ResponseEntity.internalServerError().body(new ErrorDto(BUS_API_EXCEPTION_MESSAGE));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> bindIllegalArgumentException(final IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(LoginUserNotFoundException.class)
    public ResponseEntity<ErrorDto> bindLoginUserNotFoundException(final LoginUserNotFoundException e) {
        //로그인 아이디 또는 비밀번호 에러
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> bindBadCredentialsException(final BadCredentialsException e) {
        //로그인 아이디 또는 비밀번호 에러
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDto(e.getMessage()));
    }
}
