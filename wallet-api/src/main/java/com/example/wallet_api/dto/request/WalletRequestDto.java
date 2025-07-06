package com.example.wallet_api.dto.request;

import com.example.wallet_api.enums.OperationType;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class WalletRequestDto {
    private UUID walletId;
    private OperationType operationType;
    private BigDecimal amount;
}
