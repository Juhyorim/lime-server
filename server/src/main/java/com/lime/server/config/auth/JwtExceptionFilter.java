package com.lime.server.config.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(req, res);
        } catch (ExpiredJwtException e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, req, res, "만료된 토큰입니다.");
        } catch (JwtException | IOException e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, req, res, e.getMessage());
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletRequest req, HttpServletResponse res, String errorMessage) throws IOException {
        String origin = req.getHeader("Origin");
        if ("https://tico-lime.netlify.app".equals(origin) || "http://localhost:5173".equals(origin)) {
            res.setHeader("Access-Control-Allow-Origin", origin);
            res.setHeader("Access-Control-Allow-Credentials", "true");
            res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With");
        }

        res.setStatus(status.value());
        res.setContentType("application/json; charset=UTF-8");

        JwtExceptionResponse jwtExceptionResponse = new JwtExceptionResponse(status, errorMessage);
        res.getWriter().write(jwtExceptionResponse.convertToJson());
    }
}

