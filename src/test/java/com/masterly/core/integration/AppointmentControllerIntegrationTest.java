package com.masterly.core.integration;

import com.masterly.core.config.BaseTestcontainersTest;
import com.masterly.core.dto.*;
import com.masterly.core.entity.Client;
import com.masterly.core.entity.Master;
import com.masterly.core.mapper.ClientMapper;
import com.masterly.core.mapper.MasterMapper;
import com.masterly.core.repository.AppointmentRepository;
import com.masterly.core.repository.ClientRepository;
import com.masterly.core.repository.MasterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AppointmentControllerIntegrationTest extends BaseTestcontainersTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MasterRepository masterRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private MasterMapper masterMapper;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MasterDto testMaster;
    private ClientDto testClient;
    private Long serviceId = 1L;
    private String authToken;

    @BeforeEach
    void setUp() throws Exception {
        Master master = masterRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Master with id=1 not found"));

        master.setPasswordHash(passwordEncoder.encode("123"));
        master = masterRepository.save(master);

        Client client = clientRepository.findByMasterId(master.getId()).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Client not found for master"));

        testMaster = masterMapper.toDto(master);
        testClient = clientMapper.toDto(client);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(testMaster.getEmail());
        loginRequest.setPassword("123");

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        LoginResponse response = objectMapper.readValue(loginResponse, LoginResponse.class);
        authToken = response.getToken();

        assertThat(authToken).isNotNull();
        assertThat(authToken).isNotEmpty();
    }

    @Test
    void createAppointment_ShouldReturnAppointmentDto() throws Exception {
        AppointmentCreateDto createDto = AppointmentCreateDto.builder()
                .masterId(testMaster.getId())
                .clientId(testClient.getId())
                .serviceId(serviceId)
                .appointmentDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .notes("Тестовая запись")
                .build();

        mockMvc.perform(post("/api/appointments")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.masterId").value(testMaster.getId()))
                .andExpect(jsonPath("$.clientId").value(testClient.getId()))
                .andExpect(jsonPath("$.serviceId").value(serviceId))
                .andExpect(jsonPath("$.status").value("PENDING"));

        var appointments = appointmentRepository.findByMasterId(testMaster.getId());
        assertThat(appointments).hasSize(1);
        assertThat(appointments.get(0).getNotes()).isEqualTo("Тестовая запись");
    }

    @Test
    void getAppointmentById_ShouldReturnAppointment() throws Exception {
        AppointmentCreateDto createDto = AppointmentCreateDto.builder()
                .masterId(testMaster.getId())
                .clientId(testClient.getId())
                .serviceId(serviceId)
                .appointmentDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .build();

        String response = mockMvc.perform(post("/api/appointments")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        AppointmentDto created = objectMapper.readValue(response, AppointmentDto.class);

        mockMvc.perform(get("/api/appointments/{id}", created.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.getId()))
                .andExpect(jsonPath("$.masterId").value(testMaster.getId()));
    }

    @Test
    void updateAppointmentStatus_ShouldChangeStatus() throws Exception {
        AppointmentCreateDto createDto = AppointmentCreateDto.builder()
                .masterId(testMaster.getId())
                .clientId(testClient.getId())
                .serviceId(serviceId)
                .appointmentDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .build();

        String response = mockMvc.perform(post("/api/appointments")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        AppointmentDto created = objectMapper.readValue(response, AppointmentDto.class);

        mockMvc.perform(post("/api/appointments/{id}/status", created.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .param("status", "CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void deleteAppointment_ShouldRemoveAppointment() throws Exception {
        AppointmentCreateDto createDto = AppointmentCreateDto.builder()
                .masterId(testMaster.getId())
                .clientId(testClient.getId())
                .serviceId(serviceId)
                .appointmentDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .build();

        String response = mockMvc.perform(post("/api/appointments")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        AppointmentDto created = objectMapper.readValue(response, AppointmentDto.class);

        mockMvc.perform(delete("/api/appointments/{id}", created.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNoContent());

        assertThat(appointmentRepository.findById(created.getId())).isEmpty();
    }

    @Test
    void getAppointmentsPaginated_ShouldReturnPage() throws Exception {
        for (int i = 0; i < 3; i++) {
            AppointmentCreateDto dto = AppointmentCreateDto.builder()
                    .masterId(testMaster.getId())
                    .clientId(testClient.getId())
                    .serviceId(serviceId)
                    .appointmentDate(LocalDate.now().plusDays(i + 1))
                    .startTime(LocalTime.of(10, 0))
                    .build();
            mockMvc.perform(post("/api/appointments")
                    .header("Authorization", "Bearer " + authToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)));
        }

        mockMvc.perform(get("/api/appointments/paginated")
                        .header("Authorization", "Bearer " + authToken)
                        .param("page", "0")
                        .param("size", "2")
                        .param("masterId", String.valueOf(testMaster.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void checkAvailability_ShouldReturnTrue_WhenSlotFree() throws Exception {
        mockMvc.perform(get("/api/appointments/check-availability")
                        .header("Authorization", "Bearer " + authToken)
                        .param("masterId", String.valueOf(testMaster.getId()))
                        .param("date", LocalDate.now().plusDays(1).toString())
                        .param("startTime", "10:00")
                        .param("endTime", "11:00"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}