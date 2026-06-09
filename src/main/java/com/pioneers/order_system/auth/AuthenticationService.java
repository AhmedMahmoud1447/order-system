package com.pioneers.order_system.auth;


import com.pioneers.order_system.security.jwt.JwtService;
import com.pioneers.order_system.security.user.SecurityUser;
import com.pioneers.order_system.user.Role;
import com.pioneers.order_system.user.User;
import com.pioneers.order_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // 1️⃣ منطق إنشاء حساب جديد
    public AuthenticationResponse register(AuthenticationRequest request) {
        // تأكد أولاً أن الإيميل غير مكرر (يمكنك رمي BadRequestException هنا لو مكرر)

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 👈 تشفير الباسورد حتماً!
        user.setRole(Role.CUSTOMER); // الافتراضي زبون، ويمكنك تعديلها حسب الحاجة

        userRepository.save(user);

        // توليد التوكن فوراً بعد التسجيل ليدخل السيستم مباشرة
        var securityUser = new SecurityUser(user);
        var jwtToken = jwtService.generateToken(securityUser);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    // 2️⃣ منطق تسجيل الدخول والتحقق
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // الـ AuthenticationManager هو الذي يقوم بالمقارنة خلف الستار ويرمي Exception لو الباسورد غلط
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // لو وصلنا هنا، فهذا يعني أن الباسورد صحيحة 100%
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        var securityUser = new SecurityUser(user);
        var jwtToken = jwtService.generateToken(securityUser);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}