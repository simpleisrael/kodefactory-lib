package com.kodefactory.tech.lib.security.rest;

import com.kodefactory.tech.lib.core.event.EventPublisher;
import com.kodefactory.tech.lib.core.rest.BaseREST;
import com.kodefactory.tech.lib.exception.RestException;
import com.kodefactory.tech.lib.security.dto.JwtAuthenticationRequest;
import com.kodefactory.tech.lib.security.dto.JwtAuthenticationResponse;
import com.kodefactory.tech.lib.security.dto.UserDTO;
import com.kodefactory.tech.lib.security.event.UserLoggedInEvent;
import com.kodefactory.tech.lib.security.jwt.JwtTokenUtil;
import com.kodefactory.tech.lib.security.jwt.JwtUser;
import com.kodefactory.tech.lib.security.domain.UserEO;
import com.kodefactory.tech.lib.security.repository.UserRepository;
import com.kodefactory.tech.lib.security.service.AuthenticatedUser;
import com.kodefactory.tech.lib.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("auth-service")
public class AuthenticationRestController extends BaseREST {

    @Value("${jwt.header}")
    private String tokenHeader;

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private UserDetailsService userDetailsService;
    private EventPublisher eventPublisher;
    private UserRepository userRepository;


    public AuthenticationRestController(UserService userService,
                                        AuthenticationManager authenticationManager,
                                        JwtTokenUtil jwtTokenUtil,
                                        EventPublisher eventPublisher,
                                        UserRepository userRepository,
                                        UserDetailsService userDetailsService){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
    }


    /**
     * Perform authentication request
     * @param authenticationRequest
     * @param device
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails, device);

        Optional<UserEO> userOptional = userRepository.findByEmail(authenticationRequest.getEmail());
        if(userOptional.isPresent()){
            UserEO user = userOptional.get();
            user.setLastLoginDate(new Date());
            user = userRepository.save(user);
            eventPublisher.publish(new UserLoggedInEvent<>(user));
        }

        return buildSuccessResponse(new JwtAuthenticationResponse(token));
    }


    /**
     * Perform token refresh request
     * @param request
     * @return
     */
    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }


    /**
     * Register a new user
     * @param userDTO
     * @return
     * @throws RestException
     */
    @PostMapping(value = "${jwt.route.authentication.register}")
    public ResponseEntity<Object> saveUser(@RequestBody UserDTO userDTO) throws RestException {
        return buildSuccessResponse(userService.saveUser(userDTO));
    }

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public JwtUser getAuthenticatedUser() {
        return authenticatedUser.getAuthenticatedUser();
    }

    @GetMapping("me")
    public JwtUser getMe() {
        return authenticatedUser.getAuthenticatedUser();
    }


}
