package org.Stasy.PublicPrivacyAppBackendHeroku.JWT;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.Stasy.PublicPrivacyAppBackendHeroku.entity.Opinion;
import org.Stasy.PublicPrivacyAppBackendHeroku.entity.User;
import org.Stasy.PublicPrivacyAppBackendHeroku.repository.OpinionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;



@Service
public class JwtGenerator implements JwtGeneratorInterface {
    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    OpinionsRepository opinionsRepository;

    //step1 :turn the key to byte
    private Key getSigningKey() {
        byte[] keyBytes = this.secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //step2:function to generate Token
    @Override
    public String generateLoginToken(User user) throws UnsupportedEncodingException {

        String loginToken = Jwts.builder()
                .setSubject(user.getEmail())//because when user logs in, we require password ad email
                .setIssuedAt(new Date())//now
                .claim("username", user.getUsername())
                .signWith(getSigningKey())
                .compact();

        System.out.println("loginToken printing from generateLoginToken:" + loginToken);
        return loginToken;
    }

    @Override
    public String generateDashboardToken(User user) {

        ///////list opinions from this user
        List<Opinion> opinionsByUser = opinionsRepository.findOpinionByCollaboratorName(user.getUsername());

        String dashboardToken = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("opinionsList", opinionsByUser)
                .claim("email", user.getEmail())
                .claim("password", user.getPassword())
                .claim("username", user.getUsername())
                .setIssuedAt(new Date())
                .signWith(getSigningKey())
                .compact();

        System.out.println("dashboardToken printing from JwtGenerateDashboardToken :" + dashboardToken);
        return dashboardToken;
    }
}