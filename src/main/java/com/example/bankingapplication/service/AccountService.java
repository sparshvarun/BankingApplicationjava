package com.example.bankingapplication.service;

import com.example.bankingapplication.dto.AccountDto;
import com.example.bankingapplication.dto.TransferFundDto;

import java.util.List;


public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    AccountDto getAccountByID(Long id);

    AccountDto deposit(Long id, double amount);

    AccountDto withdraw(Long id, double amount);


    List<AccountDto> getAllAccounts();

    void deleteAccount(Long id);

    void transferFunds(TransferFundDto transferFundDto);

    List<AccountDto> getAccountTransactions(Long accountId);
}
