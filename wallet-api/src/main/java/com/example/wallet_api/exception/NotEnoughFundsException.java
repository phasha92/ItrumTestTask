package com.example.wallet_api.exception;

public class NotEnoughFundsException extends RuntimeException{
    public NotEnoughFundsException(){
        super("Not enough funds.");
    }
}
