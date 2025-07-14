package com.korg.rvapi.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.korg.rvapi.user.models.UserTable;

@Repository
public interface UserRepository extends JpaRepository<UserTable, String> {
    // check duplicates
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<UserTable> findByUsername(String username);
}
