package com.example.wallet_api.service.impl;

import com.example.wallet_api.dto.request.WalletRequestDto;
import com.example.wallet_api.dto.response.WalletDto;
import com.example.wallet_api.repository.WalletJpa;
import com.example.wallet_api.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletJpa walletJpa;

    @Override
    public boolean executeWalletOperation(WalletRequestDto dto) {
        return false;
    }

    @Override
    public WalletDto getWalletByUUID(UUID uuid) {
        return null;
    }
}
