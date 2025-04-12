package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    JwtUtils jwtUtils;

    @Test
    public void whenGetUserById_thenReturnUser() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                UserDetailsImpl.builder()
                        .id(1L)
                        .username("user@test.com")
                        .password("encodedPassword")
                        .build(),
                null,
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        mockMvc.perform(get("/api/user/1")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }
}
