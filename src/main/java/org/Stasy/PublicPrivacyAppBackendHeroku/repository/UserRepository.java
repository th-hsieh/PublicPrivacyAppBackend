package org.Stasy.PublicPrivacyAppBackendHeroku.repository;

import org.Stasy.PublicPrivacyAppBackendHeroku.entity.User;
import org.Stasy.PublicPrivacyAppBackendHeroku.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmailAndPassword(String email, String password) throws UserNotFoundException;

    User findByUsername(String username);

    User findByEmail(String email);


    Optional<User> findOneByEmailAndPassword(String email, String password);

    User findByEmailIgnoreCase(String emailId);

    Boolean existsByEmail(String email);
}