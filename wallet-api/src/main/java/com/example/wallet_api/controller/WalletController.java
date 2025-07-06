package com.example.wallet_api.controller;

import com.example.wallet_api.dto.request.WalletRequestDto;
import com.example.wallet_api.dto.response.WalletDto;
import com.example.wallet_api.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("wallets/{uuid}")
    public ResponseEntity<WalletDto> getWallet(@PathVariable UUID uuid) {
        return ResponseEntity.ok(walletService.getWalletByUUID(uuid));
    }

    @PostMapping("wallet/")
    public ResponseEntity<Boolean> operationWithWalletBalance(@RequestBody @Valid WalletRequestDto dto){
        return ResponseEntity.ok(walletService.executeWalletOperation(dto));
    }
}
