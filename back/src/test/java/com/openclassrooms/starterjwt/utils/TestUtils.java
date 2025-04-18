package com.openclassrooms.starterjwt.utils;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collections;

public class TestUtils {
    public static String generateJwtForUser(JwtUtils jwtUtils, String username, String role) {
        // Créer le UserDetailsImpl
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username(username)
                .password("encodedPassword")
                .build();

        // Créer l'Authentication avec des autorités explicites
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );

        return jwtUtils.generateJwtToken(authentication);
    }
}
