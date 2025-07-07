package com.example.wallet_api.controller;

import com.example.wallet_api.dto.request.WalletRequestDto;
import com.example.wallet_api.dto.response.WalletDto;
import com.example.wallet_api.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping({"wallets","wallets/"})
    public ResponseEntity<List<WalletDto>> getAllWallets() {
        return ResponseEntity.ok(walletService.getAllWallet());
    }

    @PostMapping({"wallet/add-wallet","wallet/add-wallet/"})
    public ResponseEntity<Void> createWallet(@RequestBody WalletDto dto) {
        walletService.add(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping({"wallet","wallet/"})
    public ResponseEntity<Boolean> operationWithWalletBalance(@RequestBody @Valid WalletRequestDto dto) {
        return ResponseEntity.ok(walletService.executeWalletOperation(dto));
    }
}
