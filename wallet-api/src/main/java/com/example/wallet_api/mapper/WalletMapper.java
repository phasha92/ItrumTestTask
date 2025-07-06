package com.example.wallet_api.mapper;

import com.example.wallet_api.dto.response.WalletDto;
import com.example.wallet_api.entity.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {
    public Wallet toEntity(WalletDto responseDto) {
        return Wallet.builder()
                .walletId(responseDto.getWalletId())
                .balance(responseDto.getBalance())
                .build();
    }

    public WalletDto toDto(Wallet wallet) {
        return WalletDto.builder()
                .walletId(wallet.getWalletId())
                .balance(wallet.getBalance())
                .build();
    }
}
