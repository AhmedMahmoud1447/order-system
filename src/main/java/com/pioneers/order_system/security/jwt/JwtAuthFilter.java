package com.pioneers.order_system.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // سيسحب الـ UserDetailsServiceImpl اللي عملناه فوق

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. لو الريكويست مش باعت توكن في الهيدر.. عدي الريكويست وخليه يكمل (السبرنج هيرفضه برا لو الرابط محمي)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. قص كلمة "Bearer " وأخذ التوكن الصافي
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt); // استخراج إيميل اليوزر

        // 3. لو اليوزر موجود في التوكن ومش معمول له لوجن حالياً في الـ Context الخاص بالسيرفر
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 4. لو التوكن سليم.. لبس اليوزر كارنيه الدخول وسجله في الـ Context
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // هنا بنقول للسبرنج سيكيورتي: "اليوزر ده متأمن وخلاص عدا البوابة بنجاح"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // مرر الريكويست للفلتر اللي بعده
        filterChain.doFilter(request, response);
    }
}