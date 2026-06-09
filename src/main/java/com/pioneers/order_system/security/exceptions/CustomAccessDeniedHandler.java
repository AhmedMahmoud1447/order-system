package com.pioneers.order_system.security.exceptions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        // إعداد استجابة الـ JSON النظيفة للفرونتد
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403

        String jsonBody = String.format(
                "{\"status\": %d, \"message\": \"عذراً، لا تملك الصلاحيات الكافية لتنفيذ هذا الإجراء.\", \"timestamp\": \"%s\", \"path\": \"%s\"}",
                HttpServletResponse.SC_FORBIDDEN,
                LocalDateTime.now(),
                request.getRequestURI()
        );

        response.getWriter().write(jsonBody);
    }
}