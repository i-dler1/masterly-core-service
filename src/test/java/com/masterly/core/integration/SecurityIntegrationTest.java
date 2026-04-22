//package com.masterly.core.integration;
//
//import com.masterly.core.config.BaseTestcontainersTest;
//import com.masterly.core.dto.LoginRequest;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.masterly.core.repository.MasterRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@Transactional
//class SecurityIntegrationTest extends BaseTestcontainersTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MasterRepository masterRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @BeforeEach
//    void setUp() {
//        masterRepository.findByEmail("test@masterly.com")
//                .ifPresent(master -> {
//                    master.setPasswordHash(passwordEncoder.encode("123"));
//                    masterRepository.save(master);
//                });
//    }
//
//    @Test
//    void unauthorizedAccess_ShouldReturn403() throws Exception {
//        mockMvc.perform(get("/api/appointments"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    void publicEndpoint_ShouldBeAccessible() throws Exception {
//        mockMvc.perform(get("/api/masters"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void loginWithValidCredentials_ShouldReturnToken() throws Exception {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail("test@masterly.com");
//        loginRequest.setPassword("123");
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").exists());
//    }
//
//    @Test
//    void loginWithInvalidCredentials_ShouldReturn401() throws Exception {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail("test@masterly.com");
//        loginRequest.setPassword("wrong");
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void adminEndpoint_WithoutAdminRole_ShouldReturn403() throws Exception {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail("test@masterly.com");
//        loginRequest.setPassword("123");
//
//        String response = mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        String token = objectMapper.readTree(response).get("token").asText();
//
//        mockMvc.perform(get("/api/admin/something")
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(status().isForbidden());
//    }
//}