package com.example.wallet_api.exception;

public class WalletIsMissingException extends RuntimeException{

    public WalletIsMissingException(){
        super("Wallet is missing.");
    }
}
