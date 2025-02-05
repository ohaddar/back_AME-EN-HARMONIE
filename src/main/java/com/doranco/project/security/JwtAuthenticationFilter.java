package com.doranco.project.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtService jwtService;
    @Autowired
    UserDetailsService userDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

        // Extract the 'Authorization' header from the request
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.info("No Authorization header or it doesn't start with 'Bearer'" + authHeader);
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring("Bearer ".length());
        String email;
        try {
            // extract the username (email) from the JWT
            email = jwtService.extractUserName(jwt);
        } catch (Exception e) {
            logger.error("Failed to extract username from JWT", e);
            filterChain.doFilter(request, response);
            return;
        }

        if (email == null) {
            logger.info("No email extracted from JWT, stopping filter");
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            logger.info("User is already authenticated");
            filterChain.doFilter(request, response);
            return;
        }

        // Load the user details using the extracted email
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        boolean isTokenValid;
        try {
            isTokenValid = jwtService.isTokenValid(jwt, userDetails);
        } catch (Exception e) {
            logger.error("JWT validation failed", e);
            filterChain.doFilter(request, response);
            return;
        }

        if (!isTokenValid) {
            logger.info("Token is not valid");
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        // Set the authentication in the SecurityContext
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        logger.info("User {} authenticated successfully", email);
        filterChain.doFilter(request, response);
    }}
