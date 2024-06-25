package com.example.bankingapplication.dto;

public record TransferFundDto(Long fromAccountId, Long toAccountId, double amount) {


}
