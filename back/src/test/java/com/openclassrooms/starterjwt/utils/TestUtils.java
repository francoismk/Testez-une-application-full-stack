package com.openclassrooms.starterjwt.utils;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;

public class TestUtils {
    public static String generateJwtForUser(JwtUtils jwtUtils, String username, String role) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                UserDetailsImpl.builder()
                        .id(1L)
                        .username(username)
                        .password("encodedPassword")
                        .build(),
                null,
                Arrays.asList(new SimpleGrantedAuthority(role))
        );
        return jwtUtils.generateJwtToken(authentication);
    }
}
