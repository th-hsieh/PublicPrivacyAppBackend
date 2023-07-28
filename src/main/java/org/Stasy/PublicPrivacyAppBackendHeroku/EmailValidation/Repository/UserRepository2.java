package org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.Stasy.PublicPrivacyAppBackendHeroku.entity.User;

@Repository
public interface UserRepository2 extends JpaRepository<User, Long> {
    User findByEmailIgnoreCase(String emailId);

    Boolean existsByEmail(String email);
}