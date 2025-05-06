package com.nidhisync.billing.config;
//JwtFilter.java
import com.nidhisync.billing.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  public JwtFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req,
                                  HttpServletResponse res,
                                  FilterChain chain)
                                  throws ServletException, IOException {
    String authHeader = req.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      Claims claims = jwtUtil.validateToken(token).getBody();
      String username = claims.getSubject();
      @SuppressWarnings("unchecked")
      List<String> roles = (List<String>)claims.get("roles");
      var auth = new UsernamePasswordAuthenticationToken(
          username,
          null,
          roles.stream().map(SimpleGrantedAuthority::new).toList()
      );
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
    chain.doFilter(req, res);
  }
}
