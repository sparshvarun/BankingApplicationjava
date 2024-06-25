package com.example.bankingapplication.repository;

import com.example.bankingapplication.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> { //1. is entity name
                                                          //2. is primary key type

}
