package com.pioneers.order_system.security.exceptions;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        // إعداد استجابة الـ JSON النظيفة للفرونتد
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401

        String jsonBody = String.format(
                "{\"status\": %d, \"message\": \"عذراً، يجب عليك تسجيل الدخول أولاً للوصول إلى هذا الرابط.\", \"timestamp\": \"%s\", \"path\": \"%s\"}",
                HttpServletResponse.SC_UNAUTHORIZED,
                LocalDateTime.now(),
                request.getRequestURI()
        );

        response.getWriter().write(jsonBody);
    }
}