package org.Stasy.PublicPrivacyAppBackendHeroku.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtDecoder {
    @Value("${jwt.secret}")
    private String secretKey;

    public String decodeUserEmailFromLoginToken(String loginToken) { //when Login, I use email and password. So loginToken contains Email and Password. Now I want to decode it. So

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(loginToken);

        Claims claims = claimsJws.getBody();

        System.out.println("Hi This is from decodeUserEmailFromLoginToken in JwtDecoder, and the Claims are:");
        System.out.println(claims);

        String result=claimsJws.getBody().getSubject();//subject should be email
        System.out.println("result from getUserInfoFromLoginToken:"+result);
        return result;
    }

    public String decodeUserInfoFromDashboardToken(String dashboardToken) { //when edit/delete, I use email and password. So dashboardToken contains Email and Password and messages.

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(dashboardToken);

        Header headers = claimsJws.getHeader();
        Claims claims = claimsJws.getBody();

        System.out.println("Hi This is from decodeUserInfoFromDashboardToken in JwtDecoder,the Headers are:");
        System.out.println(headers);

        System.out.println("Hi This is from decodeUserInfoFromDashboardToken in JwtDecoder,the Claims are:");
        System.out.println(claims);

        String subject =claimsJws.getBody().getSubject();
        System.out.println("Subject from getUserInfoFromDashboardToken clasimJws:"+subject);
        return subject;
    }

}

