package com.pioneers.order_system.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 🔍 الميثود الجوهرية التي تبحث بالإيميل وترجع Optional لحمايتنا من الـ NullPointerExceptions
    Optional<User> findByEmail(String email);
}