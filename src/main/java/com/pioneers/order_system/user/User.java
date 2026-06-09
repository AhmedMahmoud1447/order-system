package com.pioneers.order_system.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users") // اسم الجدول في الداتابيز
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email; // سنستخدم الإيميل كـ Username لتسجيل الدخول

    @Column(nullable = false)
    private String password; // الباسورد المشفرة

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // رتبة المستخدم (ADMIN أو CUSTOMER)
}