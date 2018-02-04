package com.kodefactory.tech.lib.security.service;

import com.kodefactory.tech.lib.security.domain.UserEO;
import com.kodefactory.tech.lib.security.jwt.JwtTokenUtil;
import com.kodefactory.tech.lib.security.jwt.JwtUser;
import com.kodefactory.tech.lib.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticatedUser {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    public JwtUser getAuthenticatedUser() {
        HttpServletRequest servletRequest = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        if(servletRequest.getHeader(tokenHeader).length() < 7) return null;

        String token = servletRequest.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return (JwtUser) userDetailsService.loadUserByUsername(username);
    }

    public UserEO getAuthenticatedUserEO(){
        return userRepository.findByEmail(getAuthenticatedUser().getEmail()).orElse(null);
    }
}


