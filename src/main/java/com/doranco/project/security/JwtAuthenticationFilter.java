package com.doranco.project.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtService jwtService;
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    if(authHeader == null || !authHeader.startsWith("Bearer ")){
        filterChain.doFilter(request,response);
return;
    }
    final String jwt = authHeader.substring("Bearer ".length());
    final String email = jwtService.extractUserName(jwt);
    if(email == null || SecurityContextHolder.getContext().getAuthentication() != null) {
        filterChain.doFilter(request,response);
        return;
    }
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
    if(!jwtService.isTokenValid(jwt,userDetails)) {
        filterChain.doFilter(request,response);
        return;
    }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,userDetails.getAuthorities()
        );
    authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
    );
    SecurityContextHolder.getContext().setAuthentication(authToken);
filterChain.doFilter(request,response);
    }
}
