package com.example.wallet_api.service.impl;

import com.example.wallet_api.dto.request.WalletRequestDto;
import com.example.wallet_api.dto.response.WalletDto;
import com.example.wallet_api.entity.Wallet;
import com.example.wallet_api.exception.WalletIsMissingException;
import com.example.wallet_api.mapper.WalletMapper;
import com.example.wallet_api.repository.WalletJpa;
import com.example.wallet_api.service.WalletService;
import com.example.wallet_api.wallet_strategy.WalletOperationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletServiceImpl implements WalletService {

    private final WalletJpa walletJpa;
    private final WalletMapper mapper;

    @Override
    public void add(WalletDto dto) {
        walletJpa.save(mapper.toEntity(dto));
    }

    private final WalletOperationContext context;

    @Override
    public boolean executeWalletOperation(WalletRequestDto dto) {

        return context.execute(dto);
    }

    @Override
    public WalletDto getWalletByUUID(UUID uuid) {
        Optional<Wallet> wallet = walletJpa.findById(uuid);

        if (wallet.isPresent()) {
            return mapper.toDto(wallet.get());
        } else {
            throw new WalletIsMissingException();
        }
    }

    @Override
    public List<WalletDto> getAllWallet() {
        return walletJpa.findAll().stream().map(mapper::toDto).toList();
    }
}
