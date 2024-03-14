package com.barcode.QrCodeGenerator.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private final String SECRET_KEY =
      "f59d0703f2a6aab8081c7f1945bdd20de361de9215eb4765952e00853ea71ed0";

  public String extractUserName(String token) {
    return this.extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return this.extractClaim(token, Claims::getExpiration);
  }

  public Date extractIssueDate(String token) {
    return this.extractClaim(token, Claims::getIssuedAt);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails) {
    return this.generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {
    return Jwts.builder()
        .setClaims(extractClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
        .signWith(getSigninKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigninKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUserName(token);
    return username.equals(userDetails.getUsername()) && !this.isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Key getSigninKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
