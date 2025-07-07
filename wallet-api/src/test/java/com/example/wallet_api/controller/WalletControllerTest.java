package com.example.wallet_api.controller;

import com.example.wallet_api.dto.request.CreateWalletDto;
import com.example.wallet_api.dto.request.WalletRequestDto;
import com.example.wallet_api.dto.response.WalletDto;
import com.example.wallet_api.enums.OperationType;
import com.example.wallet_api.exception.InsufficientFundsException;
import com.example.wallet_api.exception.WalletIsMissingException;
import com.example.wallet_api.service.WalletService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
@AutoConfigureMockMvc
class WalletControllerTest {

    @MockBean
    WalletService service;
    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Get /wallets")
    void testGetAllWallet() throws Exception {
        List<WalletDto> wallets = List.of(
                WalletDto.builder().build(),
                WalletDto.builder().build()
        );

        when(service.getAllWallet()).thenReturn(wallets);

        mvc.perform(get("/api/v1/wallets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(wallets.size()));
    }

    @Test
    @DisplayName("GET /wallets/{uuid} return 200")
    void testGetWalletByUUIDSuccess() throws Exception {
        UUID uuid = UUID.randomUUID();
        WalletDto dto = WalletDto.builder()
                .walletId(uuid)
                .balance(BigDecimal.TEN)
                .build();

        when(service.getWalletByUUID(uuid)).thenReturn(dto);

        mvc.perform(get("/api/v1/wallets/" + uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(uuid.toString()))
                .andExpect(jsonPath("$.balance").value("10"));
    }

    @Test
    @DisplayName("GET /wallets/{uuid} return 404")
    void testGetWalletByUUIDNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(service.getWalletByUUID(uuid)).thenThrow(new WalletIsMissingException());

        mvc.perform(get("/api/v1/wallets/" + uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /wallets/{uuid} return 400(invalid uuid)")
    void testGetWalletByInvalidUUID() throws Exception {
        mvc.perform(get("/api/v1/wallets/qwerty"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /wallet/add-wallet return 201")
    void testCreateWalletSuccess() throws Exception {

        CreateWalletDto dto = CreateWalletDto.builder().balance(BigDecimal.ONE).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        mvc.perform(post("/api/v1/wallet/add-wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /wallet/add-wallet return 400(empty body)")
    void testCreateWalletWithEmptyBody() throws Exception {

        CreateWalletDto dto = CreateWalletDto.builder().build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        mvc.perform(post("/api/v1/wallet/add-wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /wallet/add-wallet return 400(negative balance)")
    void testCreateWalletWithNegativeBalance() throws Exception {

        CreateWalletDto dto = CreateWalletDto.builder().balance(BigDecimal.valueOf(-1)).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        mvc.perform(post("/api/v1/wallet/add-wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /wallet/add-wallet return 400(balance is un correct)")
    void testCreateWalletWithUnCorrectBalance() throws Exception {

        String json = """
                {
                 "balance": "qwerty"
                }
                """;

        mvc.perform(post("/api/v1/wallet/add-wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /wallet return 400(operation type is null)")
    void testOperationWithNullOperationType() throws Exception {

        WalletRequestDto dto = WalletRequestDto.builder()
                .operationType(null)
                .walletId(UUID.randomUUID())
                .amount(BigDecimal.ONE)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dto);

        mvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /wallet return 400(operation type is UNCOWN)")
    void testOperationWithUncownOperationType() throws Exception {

        UUID uuid = UUID.randomUUID();

        String json = """
                {
                    "walletId": "{uuid}",
                    "operationType": "UNCOWN",
                    "amount": 100
                }
                """;
        json = json.replace("{uuid}", uuid.toString());

        mvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /wallet return 200 (valid deposit)")
    void testValidDeposit() throws Exception {

        WalletRequestDto dto = WalletRequestDto.builder()
                .operationType(OperationType.DEPOSIT)
                .walletId(UUID.randomUUID())
                .amount(BigDecimal.valueOf(100))
                .build();

        when(service.executeWalletOperation(any())).thenReturn(true);

        String json = new ObjectMapper().writeValueAsString(dto);

        mvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("POST /wallet return 400 (empty amount)")
    void testEmptyAmount() throws Exception {

        UUID uuid = UUID.randomUUID();

        String json = """
                {
                    "walletId": "{uuid}",
                    "operationType": "DEPOSIT",
                    "amount": ""
                }
                """;
        json = json.replace("{uuid}", uuid.toString());

        mvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /wallet return 400 (null amount)")
    void testNullAmount() throws Exception {

        WalletRequestDto dto = WalletRequestDto.builder()
                .operationType(OperationType.DEPOSIT)
                .walletId(UUID.randomUUID())
                .amount(null)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dto);

        mvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /wallet return 400 (invalid amount format)")
    void testInvalidAmountFormat() throws Exception {

        UUID uuid = UUID.randomUUID();

        String json = """
                {
                    "walletId": "{uuid}",
                    "operationType": "DEPOSIT",
                    "amount": "qwerty"
                }
                """;
        json = json.replace("{uuid}", uuid.toString());

        mvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /wallet return 400 (empty UUID)")
    void testEmptyUuid() throws Exception {

        String json = """
                {
                    "walletId": "",
                    "operationType": "DEPOSIT",
                    "amount": 100
                }
                """;

        mvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /wallet return 400 (null UUID)")
    void testNullUuid() throws Exception {

        WalletRequestDto dto = WalletRequestDto.builder()
                .operationType(OperationType.DEPOSIT)
                .walletId(null)
                .amount(BigDecimal.valueOf(100))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dto);

        mvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /wallet return 400 (invalid UUID format)")
    void testInvalidUuidFormat() throws Exception {

        String json = """
                {
                    "walletId": "qwerty",
                    "operationType": "DEPOSIT",
                    "amount": 100
                }
                """;

        mvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /wallet return 200 (valid withdraw)")
    void testValidWithdraw() throws Exception {

        WalletRequestDto dto = WalletRequestDto.builder()
                .walletId(UUID.randomUUID())
                .operationType(OperationType.WITHDRAW)
                .amount(BigDecimal.TEN)
                .build();

        when(service.executeWalletOperation(any())).thenReturn(true);

        String json = new ObjectMapper().writeValueAsString(dto);

        mvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("POST /wallet return 200 (valid withdraw)")
    void testWithdrawWithInsufficientFunds() throws Exception {

        WalletRequestDto dto = WalletRequestDto.builder()
                .walletId(UUID.randomUUID())
                .operationType(OperationType.WITHDRAW)
                .amount(BigDecimal.TEN)
                .build();

        when(service.executeWalletOperation(any())).thenThrow(new InsufficientFundsException());

        String json = new ObjectMapper().writeValueAsString(dto);

        mvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity());
    }

}
