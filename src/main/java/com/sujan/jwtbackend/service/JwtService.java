package com.sujan.jwtbackend.service;

import com.sujan.jwtbackend.dto.JwtRequest;
import com.sujan.jwtbackend.dto.JwtResponse;
import com.sujan.jwtbackend.model.User;
import com.sujan.jwtbackend.repository.UserRepository;
import com.sujan.jwtbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final AuthenticationConfiguration authenticationConfiguration;

    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String userName = jwtRequest.getUserName();
        String userPassword = jwtRequest.getUserPassword();
        authenticate(userName, userPassword);

        UserDetails userDetails = loadUserByUsername(userName);
        String token = jwtUtil.generateToken(userDetails);

        User user = userRepository.findById(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new JwtResponse(user, token);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUserName(),
                        user.getUserPassword(),
                        getAuthorities(user)))
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    private Set<SimpleGrantedAuthority> getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName())));
        return authorities;
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            AuthenticationManager authManager = authenticationConfiguration.getAuthenticationManager();
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("User is disabled", e);
        } catch (BadCredentialsException e) {
            throw new Exception("Bad credentials", e);
        }
    }
}
