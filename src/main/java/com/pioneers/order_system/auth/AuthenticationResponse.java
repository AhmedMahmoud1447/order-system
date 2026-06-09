package com.pioneers.order_system.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token; // 👈 التوكن الذي سيأخذه الأنجولار ويخزنه عنده
}