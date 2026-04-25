package com.pioneers.order_system.exceptions;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@Builder
public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private int statusCode;
}
