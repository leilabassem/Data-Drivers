package com.example.Store.repository;
import com.example.Store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);

    // Check if email already exists 
    boolean existsByEmail(String email);

    // Check if username already exists
    boolean existsByUsername(String username);
}


