package com.example.wallet_api.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class WalletDto {
    private UUID walletId;
    private BigDecimal balance;
}
