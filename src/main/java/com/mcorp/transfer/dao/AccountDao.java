package com.mcorp.transfer.dao;

import com.mcorp.transfer.dao.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao {

    void updateAccount(Account account);

    Optional<Account> findAccountByNumberAndSortCode(String accountNumber, String sortCode);

    void save(Account account);
}
