package com.example.wallet_api.service;


import com.example.wallet_api.dto.request.WalletRequestDto;
import com.example.wallet_api.dto.response.WalletDto;

import java.util.UUID;

public interface WalletService {
    WalletDto getWalletByUUID(UUID uuid);
    boolean executeWalletOperation(WalletRequestDto dto);
}
