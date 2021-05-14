package com.plawande.springsecurityjwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plawande.springsecurityjwt.models.AuthenticationRequest;
import com.plawande.springsecurityjwt.service.MyUserDetailsService;
import com.plawande.springsecurityjwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private MyUserDetailsService myUserDetailsService;

    private JwtUtil jwtUtil;

    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                   MyUserDetailsService myUserDetailsService,
                                                   JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/authenticate", HttpMethod.POST.toString()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            /*AuthenticationRequest authenticationRequest = new ObjectMapper()
                    .readValue(request.getReader().lines().collect(Collectors.joining()), AuthenticationRequest.class);*/
            AuthenticationRequest authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), AuthenticationRequest.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Incorrect username and password", e);
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse input credentials object", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authResult.getName());
        final String jwtToken = jwtUtil.generateToken(userDetails);
        response.addHeader("Authorization", jwtToken);
    }
}
