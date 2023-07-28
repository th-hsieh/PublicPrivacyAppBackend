package org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Repository;

import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Entity.ConfirmationToken2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("confirmationTokenRepository")
public interface ConfirmationTokenRepository2 extends JpaRepository<ConfirmationToken2,Long> {
    ConfirmationToken2 findByConfirmationToken(String confirmationToken);
}
