package com.example.wallet_api.service;

@FunctionalInterface
public interface InsertService<T> {
    void add(T dto);
}
