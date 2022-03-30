package com.devbaktiyarov.security.oauthauthorizationserver.repository;

import com.devbaktiyarov.security.oauthauthorizationserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
