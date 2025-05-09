package com.nidhisync.billing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nidhisync.billing.dto.AuthRequestDto;
import com.nidhisync.billing.dto.RegisterRequest;

@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationTest {

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper mapper;

  private String jwt;

  @Test @Order(1)
  void registerAndLogin() throws Exception {
    // Register user with a valid password (at least 6 characters)
    mvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(
          new RegisterRequest("testu", "t@e.com","+911111111111", "secure123"))))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.message").value("Registration successful"));

    // Login and retrieve JWT token
    MvcResult res = mvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(
          new AuthRequestDto("testu", "secure123"))))
      .andExpect(status().isOk())
      .andReturn();

    var body = mapper.readTree(res.getResponse().getContentAsString());
    jwt = body.get("token").asText();
    Assertions.assertNotNull(jwt);
  }

  @Test @Order(2)
  void productsCrud() throws Exception {
    // GET empty list of products
    mvc.perform(get("/api/products")
        .header("Authorization", "Bearer " + jwt))
      .andExpect(status().isOk())
      .andExpect(content().json("[]"));

    // POST a new product
    mvc.perform(post("/api/products")
        .header("Authorization", "Bearer " + jwt)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\":\"Item\",\"price\":5.0,\"stock\":10}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").isNumber())
      .andExpect(jsonPath("$.barcode").isString());
  }

  @Test @Order(3)
  void invalidJwtTest() throws Exception {
    // Test invalid JWT token
    mvc.perform(get("/api/products")
        .header("Authorization", "Bearer " + "invalid-token"))
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("$.message").value("Unauthorized"));
  }
}