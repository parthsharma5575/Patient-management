package com.pm.authservice.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key;
    JwtUtil(@Value("${jwt.secret}") String jwtSecret) {
        byte[]keyBytes= Base64.getEncoder().encode(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)//mainly the id of person the token will belong to
                .claim("role",role)//additional functionality or constraint
                .issuedAt(new Date())//date of issue
                .expiration(new Date(System.currentTimeMillis()+1000*60*60*10))//10hrs
                .signWith(key)//using secret key to sign the token->only server knows the key
                .compact();//function to complete and package the token final call
    }

    public void validateToken(String token) throws JwtException {
        try{
            //parse the token->verify the signature using secret key our environment variable
            //if signature is valid then return claims
            Jwts.parser().verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token);
        }catch(SignatureException e){
            throw new JwtException("Invalid JWT signature");
        }
        catch (JwtException e){
            throw new JwtException("Invalid JWT token");
        }
    }
}
