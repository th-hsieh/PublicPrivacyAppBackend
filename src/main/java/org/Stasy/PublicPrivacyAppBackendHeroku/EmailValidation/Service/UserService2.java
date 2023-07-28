package org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Service;

import org.Stasy.PublicPrivacyAppBackendHeroku.entity.User;
import org.springframework.http.ResponseEntity;

public interface UserService2 {
    ResponseEntity<?> saveUser(User user);

    ResponseEntity<?> confirmEmail(String confirmationToken);
}
