package com.example.wallet_api.wallet_strategy.impl;

import com.example.wallet_api.dto.request.WalletRequestDto;
import com.example.wallet_api.entity.Wallet;
import com.example.wallet_api.exception.InsufficientFundsException;
import com.example.wallet_api.repository.WalletJpa;
import com.example.wallet_api.wallet_strategy.WalletOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("withdraw")
@RequiredArgsConstructor
public class WalletOperationWithdraw implements WalletOperation {

    private final WalletJpa walletJpa;

    @Override
    public boolean execute(WalletRequestDto requestDto) {
        Optional<Wallet> walletOpt = walletJpa.findById(requestDto.getWalletId());

        if (walletOpt.isPresent()) {
            Wallet wallet = walletOpt.get();

            if (wallet.getBalance().compareTo(requestDto.getAmount()) < 0) {
                throw new InsufficientFundsException();
            }

            wallet.setBalance(wallet.getBalance().subtract(requestDto.getAmount()));
            walletJpa.save(wallet);
            return true;
        }
        return false;
    }
}

