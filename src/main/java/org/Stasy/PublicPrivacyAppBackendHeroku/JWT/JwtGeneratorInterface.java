package org.Stasy.PublicPrivacyAppBackendHeroku.JWT;

import org.Stasy.PublicPrivacyAppBackendHeroku.entity.User;

import java.io.UnsupportedEncodingException;

public interface JwtGeneratorInterface {

    String generateLoginToken(User user) throws UnsupportedEncodingException;

    String generateDashboardToken(User user) throws UnsupportedEncodingException;
}
