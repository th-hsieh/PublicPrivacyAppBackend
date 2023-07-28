package org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Service;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Entity.ConfirmationToken2;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Repository.ConfirmationTokenRepository2;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Repository.UserRepository2;
import org.Stasy.PublicPrivacyAppBackendHeroku.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl2 implements UserService2 {

    @Autowired
    private UserRepository2 userRepository2;

    @Autowired
    ConfirmationTokenRepository2 confirmationTokenRepository2;

    @Autowired
    EmailService2 emailService2;

    @Override
    public ResponseEntity<?> saveUser(User user) {

        if (userRepository2.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        userRepository2.save(user);

        ConfirmationToken2 confirmationToken = new ConfirmationToken2(user);

        confirmationTokenRepository2.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                +"https://public-privacy-app-backend-8a76ad4a141e.herokuapp.com/confirm-account?token="+confirmationToken.getConfirmationToken());
        emailService2.sendEmail(mailMessage);

        System.out.println("Confirmation Token: " + confirmationToken.getConfirmationToken());

        return ResponseEntity.ok("Verify email by the link sent on your email address");
    }

    @Override
    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken2 token = confirmationTokenRepository2.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            User user = userRepository2.findByEmailIgnoreCase(token.getUserEntity().getEmail());
            user.setEnabled(true);
            userRepository2.save(user);
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }
}
