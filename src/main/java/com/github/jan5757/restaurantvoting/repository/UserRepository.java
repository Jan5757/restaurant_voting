package com.github.jan5757.restaurantvoting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.jan5757.restaurantvoting.model.User;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User> {

    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email) ")
    Optional<User> findByEmailIgnoreCase(String email);
}
