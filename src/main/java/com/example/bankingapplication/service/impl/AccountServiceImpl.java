package com.example.bankingapplication.service.impl;

import com.example.bankingapplication.dto.AccountDto;
import com.example.bankingapplication.dto.TransactionDto;
import com.example.bankingapplication.dto.TransferFundDto;
import com.example.bankingapplication.entity.Account;
import com.example.bankingapplication.entity.Transaction;
import com.example.bankingapplication.exception.AccountException;
import com.example.bankingapplication.mapper.AccountMapper;
import com.example.bankingapplication.repository.AccountRepository;
import com.example.bankingapplication.repository.TransactionRepository;
import com.example.bankingapplication.service.AccountService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    private TransactionRepository transactionRepository;

    private static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
    private static final String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW";
    private static final String TRANSACTION_TYPE_TRANSFER = "TRANSFER";


    public AccountServiceImpl(AccountRepository accountRepository
            , TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }



    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account acoount = AccountMapper.mapToAccount(accountDto);

        Account savedAccount =accountRepository.save(acoount);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountByID(Long id) {

       Account account= accountRepository.findById(id).orElseThrow(() -> new AccountException("Account not found"));
        return AccountMapper.mapToAccountDto(account);


    }

    @Override
    public AccountDto deposit(Long id, double amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account not found"));
        double total = account.getBalance() + amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TRANSACTION_TYPE_DEPOSIT);
        transaction.setTimestamp(LocalDateTime.now());


        transactionRepository.save(transaction);

        return AccountMapper.mapToAccountDto(savedAccount);


    }

    @Override
    public AccountDto withdraw(Long id, double amount) {

        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account not found"));

        if(account.getBalance() < amount){
            throw new RuntimeException("Insufficient balance");
        }
        double total = account.getBalance() - amount;
        account.setBalance(total);

       Account savedAccount = accountRepository.save(account);


        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TRANSACTION_TYPE_WITHDRAW);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);

       return AccountMapper.mapToAccountDto(savedAccount);



    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts= accountRepository.findAll();
        return accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account))
                .collect(Collectors.toList());

    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account not found"));

        accountRepository.delete(account);


    }

    @Override
    public void transferFunds(TransferFundDto transferFundDto) {




        //Retreive the account from which we send the amount

      Account fromAccount =  accountRepository.findById(transferFundDto.fromAccountId()).orElseThrow(() -> new AccountException("Account not found"));

      //Retrieve the account to which we send the amount

        Account toAccount =  accountRepository.findById(transferFundDto.toAccountId()).orElseThrow(() -> new AccountException("Account not found"));

        if(fromAccount.getBalance() < transferFundDto.amount()){
            throw new RuntimeException("Insufficient balance");
        }


        //Debit the amount from fromAccount object

        fromAccount.setBalance(fromAccount.getBalance() - transferFundDto.amount());

        //credit the amount to toAccount object

        toAccount.setBalance(toAccount.getBalance() + transferFundDto.amount());

        accountRepository.save(fromAccount);

        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setAccountId(transferFundDto.fromAccountId());
        transaction.setAmount(transferFundDto.amount());
        transaction.setTransactionType(TRANSACTION_TYPE_TRANSFER);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);

    }

    @Override
    public List<TransactionDto> getAccountTransactions(Long accountId) {

        List<Transaction> transactions = transactionRepository
                .findByAccountIdOrderByTimestampDesc(accountId);

        return transactions.stream()
                .map((transaction) -> convertEntityToDto(transaction))
                .collect(Collectors.toList());
    }

    private TransactionDto convertEntityToDto(Transaction transaction){
        return new TransactionDto(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTimestamp()
        );
    }
}
