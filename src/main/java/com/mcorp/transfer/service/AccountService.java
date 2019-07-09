package com.mcorp.transfer.service;

import com.mcorp.transfer.model.AccountDto;

import java.math.BigDecimal;

public interface AccountService {

    void updateBalance(AccountDto from, AccountDto to, BigDecimal amount);
}
