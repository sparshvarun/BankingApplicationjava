package com.example.bankingapplication.mapper;

import com.example.bankingapplication.dto.AccountDto;
import com.example.bankingapplication.entity.Account;

public class AccountMapper {


    public static Account mapToAccount(AccountDto accountDto) {  //converted account dto to account jpa entity
        Account account = new Account(
                accountDto.id(),
                accountDto.accountHolderName(),
                accountDto.balance()
        );

        return account;
    }

    //converted account jpa entity to account dto
    public static AccountDto mapToAccountDto(Account account) {
        AccountDto accountDto = new AccountDto(
                account.getId(),
                account.getAccountHolderName(),
                account.getBalance()
        );

        return accountDto;
    }
}
