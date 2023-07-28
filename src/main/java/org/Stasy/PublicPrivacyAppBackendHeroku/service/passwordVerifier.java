package org.Stasy.PublicPrivacyAppBackendHeroku.service;

import org.springframework.security.crypto.bcrypt.BCrypt;


public class passwordVerifier {
    public static boolean verifyPassword(String enteredPassword, String hashedPassword) {
        BCrypt bCrypt = null;
        return bCrypt.checkpw(enteredPassword, hashedPassword);//true or false
    }
}
