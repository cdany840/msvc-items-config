package com.dmiranda.springcloud.mscv.oauth.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.http.MediaType;

import com.dmiranda.springcloud.mscv.oauth.models.User;

import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Service
public class UsersService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(UsersService.class);

    @Autowired
    private WebClient.Builder client;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Llamada a método UsersService::loadUserByUsername()");
        try {
            Map<String, String> params = new HashMap<>();
            params.put("username", username);

            User user = client.build().get()
                            .uri("/username/{username}", params)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(User.class)
                            .block();

            List<GrantedAuthority> roles = user.getRoles().stream()
                                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                                            .collect(Collectors.toList());

            org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(), 
                user.getPassword(), 
                user.isEnabled(), 
                true,
                true, 
                true, 
                roles);

            logger.info("Login éxitoso: {}", user);

            return userDetails;
        } catch (WebClientResponseException e) {
            String errorMessage = "Error en el login, no existe: " + username + " en el sistema";
            logger.error(errorMessage);
            throw new UsernameNotFoundException("El usuario " + username + " no existe en el sistema..." );
        }
    }
}
