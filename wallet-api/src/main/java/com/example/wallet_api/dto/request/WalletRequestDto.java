package com.example.wallet_api.dto.request;

import com.example.wallet_api.enums.OperationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class WalletRequestDto {

    @NotNull
    private UUID walletId;

    @NotNull
    private OperationType operationType;

    @Positive
    @NotNull
    private BigDecimal amount;
}
