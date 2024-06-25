package com.example.bankingapplication.controller;

import com.example.bankingapplication.dto.AccountDto;
import com.example.bankingapplication.dto.TransactionDto;
import com.example.bankingapplication.dto.TransferFundDto;
import com.example.bankingapplication.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@RestController
@RequestMapping("api/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    //Add AccountREST API

    @PostMapping
    public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }


    //Get Account by ID REST API
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById( @PathVariable Long id) {
        AccountDto accountDto = accountService.getAccountByID(id);
        return ResponseEntity.ok(accountDto);
    }
        //deposit rest api
        @PutMapping("/{id}/deposit")
        public ResponseEntity<AccountDto> deposit(@PathVariable Long id, @RequestBody Map<String, Double> request) {

            Double amount = request.get("amount");
            AccountDto accountDto = accountService.deposit(id, amount);
            return ResponseEntity.ok(accountDto);

        }
            //withdraw rest api
            @PutMapping("/{id}/withdraw")
            public ResponseEntity<AccountDto> withdraw(@PathVariable Long id,@RequestBody Map<String, Double> request){
                Double amount = request.get("amount");
                AccountDto accountDto = accountService.withdraw(id, amount);
                return ResponseEntity.ok(accountDto);



    }

    //Get All Accounts REST API
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts(){
        List<AccountDto> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }


    //Delete Account REST API
   @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount( @PathVariable Long id){
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account deleted successfully");
    }

    //Transfer Funds REST API
    @PostMapping("/transfer")
    public ResponseEntity<String> transferFund( @RequestBody TransferFundDto transferFundDto){
        accountService.transferFunds(transferFundDto);
        return ResponseEntity.ok("Transfer Successful");
    }

    //Get Account Transactions REST API
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionDto>> fetchAccountTransactions(@PathVariable("id") Long accountId){

        List<TransactionDto> transactions = accountService.getAccountTransactions(accountId);

        return ResponseEntity.ok(transactions);
    }
}
