package com.example.wallet_api.repository;

import com.example.wallet_api.entity.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;


import java.util.Optional;
import java.util.UUID;

public interface WalletJpa extends JpaRepository<Wallet, UUID> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Wallet> findById(UUID uuid);
}
