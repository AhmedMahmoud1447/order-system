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

    public AuthenticationResponse register(AuthenticationRequest request) {

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);

        userRepository.save(user);

        var securityUser = new SecurityUser(user);
        var jwtToken = jwtService.generateToken(securityUser);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        var securityUser = new SecurityUser(user);
        var jwtToken = jwtService.generateToken(securityUser);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}