package com.example.wallet_api.wallet_strategy.impl;

import com.example.wallet_api.dto.request.WalletRequestDto;
import com.example.wallet_api.entity.Wallet;
import com.example.wallet_api.repository.WalletJpa;
import com.example.wallet_api.wallet_strategy.WalletOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("deposit")
@RequiredArgsConstructor
public class WalletOperationDeposit implements WalletOperation {

    private final WalletJpa walletJpa;

    @Override
    public boolean execute(WalletRequestDto requestDto) {
        Optional<Wallet> walletOptional = walletJpa.findById(requestDto.getWalletId());

        if (walletOptional.isPresent()) {
            Wallet wallet = walletOptional.get();
            wallet.setBalance(wallet.getBalance().add(requestDto.getAmount()));
            walletJpa.save(wallet);
            return true;
        } else return false;
    }
}
