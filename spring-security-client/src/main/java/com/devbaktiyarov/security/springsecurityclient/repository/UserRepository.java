package com.devbaktiyarov.security.springsecurityclient.repository;

import com.devbaktiyarov.security.springsecurityclient.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
