package com.nidhisync.billing.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

  private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private final long validityMs = 3600_000; // 1 hour

  public String generateToken(String username, List<String> roles) {
    return Jwts.builder()
      .setSubject(username)
      .claim("roles", roles)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + validityMs))
      .signWith(key)
      .compact();
  }

  public Jws<Claims> validateToken(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(key)
      .build()
      .parseClaimsJws(token);
  }
}