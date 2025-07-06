package com.example.wallet_api.service.impl;

import com.example.wallet_api.dto.request.WalletRequestDto;
import com.example.wallet_api.dto.response.WalletDto;
import com.example.wallet_api.entity.Wallet;
import com.example.wallet_api.mapper.WalletMapper;
import com.example.wallet_api.repository.WalletJpa;
import com.example.wallet_api.service.WalletService;
import com.example.wallet_api.wallet_strategy.WalletOperationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletJpa walletJpa;
    private final WalletMapper mapper;
    private final WalletOperationContext context;

    @Override
    @Transactional
    public boolean executeWalletOperation(WalletRequestDto dto) {
        return context.execute(dto);
    }

    @Override
    @Transactional(readOnly = true)
    public WalletDto getWalletByUUID(UUID uuid) {
        Optional<Wallet> wallet = walletJpa.findById(uuid);

        if (wallet.isPresent()){
            return mapper.toDto(wallet.get());
        } else {
            throw new RuntimeException();
        }
    }
}
