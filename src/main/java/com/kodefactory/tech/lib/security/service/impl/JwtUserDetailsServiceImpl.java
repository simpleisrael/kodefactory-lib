package com.kodefactory.tech.lib.security.service.impl;

import com.kodefactory.tech.lib.security.jwt.JwtUserFactory;
import com.kodefactory.tech.lib.security.domain.UserEO;
import com.kodefactory.tech.lib.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEO> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            return JwtUserFactory.create(userOptional.get());
        }else {
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
        }
    }
}
