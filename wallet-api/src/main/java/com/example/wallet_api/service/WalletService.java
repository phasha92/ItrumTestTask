package com.example.wallet_api.service;


import com.example.wallet_api.dto.request.WalletRequestDto;
import com.example.wallet_api.dto.response.WalletDto;

import java.util.List;
import java.util.UUID;

public interface WalletService extends InsertService<WalletDto> {
    WalletDto getWalletByUUID(UUID uuid);
    List<WalletDto> getAllWallet();
    boolean executeWalletOperation(WalletRequestDto dto);
}
