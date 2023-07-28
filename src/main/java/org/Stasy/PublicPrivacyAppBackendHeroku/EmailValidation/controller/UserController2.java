package org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.controller;

import org.Stasy.PublicPrivacyAppBackendHeroku.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Service.UserService2;

@RestController
public class UserController2 {
    @Autowired
    private UserService2 userService2;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        return userService2.saveUser(user);
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken) {
        return userService2.confirmEmail(confirmationToken);
    }

}