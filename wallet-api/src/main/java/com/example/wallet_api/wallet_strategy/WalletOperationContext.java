package com.example.wallet_api.wallet_strategy;

import com.example.wallet_api.dto.request.WalletRequestDto;
import com.example.wallet_api.enums.OperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WalletOperationContext {

    private final Map<OperationType, WalletOperation> strategies;

    public boolean execute(WalletRequestDto request) {
        return strategies.get(request.getOperationType()).execute(request);
    }

}
