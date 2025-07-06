package com.example.wallet_api.exception.handler;

import com.example.wallet_api.dto.response.ErrorResponse;
import com.example.wallet_api.exception.InsufficientFundsException;
import com.example.wallet_api.exception.WalletIsMissingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WalletExceptionHandler {

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFundsException(InsufficientFundsException ex) {
        return errorResponse(ex, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(WalletIsMissingException.class)
    public ResponseEntity<ErrorResponse> handleWalletIsMissingException(WalletIsMissingException ex) {
        return errorResponse(ex, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ErrorResponse> errorResponse(RuntimeException ex, HttpStatus status){
        ErrorResponse response = new ErrorResponse(ex.getMessage(), status.value());
        return new ResponseEntity<>(response,status);
    }
}
