package com.example.wallet_api.wallet_strategy;

import com.example.wallet_api.dto.request.WalletRequestDto;

@FunctionalInterface
public interface WalletOperation {
    boolean execute(WalletRequestDto requestDto);
}
