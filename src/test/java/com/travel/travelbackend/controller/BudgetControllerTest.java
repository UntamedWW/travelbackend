package com.travel.travelbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.travelbackend.dto.BudgetRequest;
import com.travel.travelbackend.dto.BudgetResponse;
import com.travel.travelbackend.entity.BudgetType;
import com.travel.travelbackend.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BudgetController(budgetService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateBudgetSuccessfully() throws Exception {
        BudgetRequest request = createRequest();
        BudgetResponse response = createResponse();

        when(budgetService.create(any(BudgetRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.type").value("BUDGET"))
                .andExpect(jsonPath("$.amount").value(5000.00))
                .andExpect(jsonPath("$.tripId").value(1));

        verify(budgetService).create(any(BudgetRequest.class));
    }

    @Test
    void shouldEditBudgetSuccessfully() throws Exception {
        BudgetRequest request = createRequest();
        BudgetResponse response = createResponse();

        when(budgetService.edit(any(Long.class), any(BudgetRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/budgets/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.type").value("BUDGET"));

        verify(budgetService).edit(any(Long.class), any(BudgetRequest.class));
    }

    @Test
    void shouldGetBudgetsByTripAndUserSuccessfully() throws Exception {
        when(budgetService.getByTripAndUser(1L, 2L))
                .thenReturn(List.of(createResponse()));

        mockMvc.perform(get("/api/budgets/trip/1/user/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].userId").value(2))
                .andExpect(jsonPath("$[0].tripId").value(1));

        verify(budgetService).getByTripAndUser(1L, 2L);
    }

    @Test
    void shouldDeleteBudgetSuccessfully() throws Exception {
        mockMvc.perform(delete("/api/budgets/10"))
                .andExpect(status().isNoContent());

        verify(budgetService).delete(10L);
    }

    private BudgetRequest createRequest() {
        BudgetRequest request = new BudgetRequest();
        request.setUserId(2L);
        request.setType(BudgetType.BUDGET);
        request.setAmount(new BigDecimal("5000.00"));
        request.setTripId(1L);
        return request;
    }

    private BudgetResponse createResponse() {
        return new BudgetResponse(
                10L,
                2L,
                BudgetType.BUDGET,
                null,
                new BigDecimal("5000.00"),
                1L
        );
    }
}
