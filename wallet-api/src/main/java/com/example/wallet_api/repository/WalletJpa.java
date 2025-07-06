package com.example.wallet_api.repository;


import com.example.wallet_api.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface WalletJpa extends JpaRepository<Wallet, UUID> {
}
