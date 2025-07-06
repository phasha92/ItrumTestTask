package com.example.wallet_api.wallet_strategy.impl;

import com.example.wallet_api.dto.request.WalletRequestDto;
import com.example.wallet_api.entity.Wallet;
import com.example.wallet_api.repository.WalletJpa;
import com.example.wallet_api.wallet_strategy.WalletOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service("deposit")
@RequiredArgsConstructor
public class WalletOperationDeposit implements WalletOperation {

    private final WalletJpa walletJpa;

    @Override
    public boolean execute(WalletRequestDto requestDto) {
        UUID uuid = requestDto.getWalletId();

        if (uuid == null) throw new RuntimeException();

        if (requestDto.getAmount() == null || requestDto.getAmount().signum() <= 0) {
            throw new RuntimeException();
        }

        Optional<Wallet> walletOptional = walletJpa.findById(uuid);

        if (walletOptional.isPresent()) {
            Wallet wallet = walletOptional.get();
            wallet.setBalance(wallet.getBalance().add(requestDto.getAmount()));
            walletJpa.save(wallet);
            return true;
        } else return false;
    }
}
