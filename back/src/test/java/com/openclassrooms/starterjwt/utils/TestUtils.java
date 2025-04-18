package com.openclassrooms.starterjwt.utils;

import com.jayway.jsonpath.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUtils {
    public static String obtainJwtToken(MockMvc mvc, String email, String password) throws Exception {
        String loginJson = """
            {
              "email":"%s",
              "password":"%s"
            }
            """.formatted(email, password);

        MvcResult result = mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        String token = JsonPath.parse(body).read("$.token");
        return "Bearer " + token;
    }
}
