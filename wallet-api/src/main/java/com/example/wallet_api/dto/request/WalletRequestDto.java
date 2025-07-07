package com.example.wallet_api.dto.request;

import com.example.wallet_api.enums.OperationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class WalletRequestDto {

    @NotNull(message = "Identifier cannot be null.")
    private UUID walletId;

    @NotNull(message = "Operation type cannot be null.")
    private OperationType operationType;

    @Positive(message = "Balance cannot be negative.")
    @NotNull(message = "Amount cannot be null.")
    private BigDecimal amount;
}
