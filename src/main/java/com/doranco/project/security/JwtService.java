package com.doranco.project.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    public final static SignatureAlgorithm alg = SignatureAlgorithm.HS256;
    private final static SecretKey SECRET_KEY = Keys.secretKeyFor(alg);

    public String generateToken(UserDetails userDetails) {
        return generateToken( userDetails, new HashMap<>());
    }
    public String generateToken(UserDetails userDetails, Map<String ,String> extraClaims){
        return Jwts.builder().setClaims(extraClaims).
                setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5))
                .signWith(SECRET_KEY,alg)
                .compact();
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    public String extractUserName(String token) {

        return extractClaim(token, Claims::getSubject);
    }
    public boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY)
                .build().parseClaimsJwt(token)
                .getBody();
    }
}
