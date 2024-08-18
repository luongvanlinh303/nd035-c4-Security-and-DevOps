package com.udacity.ecommerce.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthenticationVerificationFilter extends BasicAuthenticationFilter {
  public JWTAuthenticationVerificationFilter(AuthenticationManager manager) {
    super(manager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    String header = request.getHeader(SecurityConstants.HEADER_STRING);

    if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
    String token = req.getHeader(SecurityConstants.HEADER_STRING);

    if (token != null) {
      String user = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
          .build()
          .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
          .getSubject();

      if (user != null)
        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

      return null;
    }

    return null;
  }
}
