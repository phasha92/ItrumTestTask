package com.example.wallet_api.config;

import com.example.wallet_api.enums.OperationType;
import com.example.wallet_api.wallet_strategy.WalletOperation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WalletBeansConfig {

    @Bean
    Map<OperationType, WalletOperation> walletOperationStrategyContext(@Qualifier("deposit") WalletOperation deposit,
                                                                       @Qualifier("withdraw") WalletOperation withdraw) {
        Map<OperationType, WalletOperation> contextStrategies = new HashMap<>();
        contextStrategies.put(OperationType.DEPOSIT, deposit);
        contextStrategies.put(OperationType.WITHDRAW, withdraw);
        return contextStrategies;
    }
}
