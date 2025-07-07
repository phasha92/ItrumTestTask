package com.example.wallet_api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CreateWalletDto {
    @Positive(message = "Balance cannot be negative.")
    @NotNull(message = "Balance cannot be null.")
    BigDecimal balance;
}
