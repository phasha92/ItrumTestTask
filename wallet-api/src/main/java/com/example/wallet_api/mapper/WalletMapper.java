package com.example.wallet_api.mapper;

import com.example.wallet_api.dto.request.CreateWalletDto;
import com.example.wallet_api.dto.response.WalletDto;
import com.example.wallet_api.entity.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {
    public Wallet toEntity(WalletDto dto) {
        return Wallet.builder()
                .walletId(dto.getWalletId())
                .balance(dto.getBalance())
                .build();
    }

    public Wallet toEntity(CreateWalletDto dto) {
        return Wallet.builder()
                .balance(dto.getBalance())
                .build();
    }

    public WalletDto toDto(Wallet wallet) {
        return WalletDto.builder()
                .walletId(wallet.getWalletId())
                .balance(wallet.getBalance())
                .build();
    }
}
