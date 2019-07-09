package com.mcorp.transfer.service.impl;

import com.google.inject.persist.Transactional;
import com.mcorp.transfer.dao.AccountDao;
import com.mcorp.transfer.dao.entity.Account;
import com.mcorp.transfer.model.AccountDto;
import com.mcorp.transfer.exception.TransferServiceException;
import com.mcorp.transfer.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.utils.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@Singleton
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    public static final String ACCOUNT_NUMBER_REGEX = "^(\\d){8}$";
    public static final String SORT_CODE_REGEX = "^(\\d){6}$";

    private final AccountDao accountDao;

    @Inject
    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Transactional
    @Override
    public void updateBalance(AccountDto from, AccountDto to, BigDecimal amount) {

        logger.debug("updateBalance starts with params {}, {}, {}", from, to, amount);
        Account fromAccount = findAccount(from.getNumber(), from.getSortCode());
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new TransferServiceException("From account has insufficient fund");
        }

        Account toAccount = findAccount(to.getNumber(), to.getSortCode());

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        updateAccounts(fromAccount, toAccount);
        logger.debug("updateBalance ends.");
    }

    private Account findAccount(String accountNumber, String sortCode) {
        validateAccountNumberAndSortCode(accountNumber, sortCode);
        return accountDao.findAccountByNumberAndSortCode(accountNumber, sortCode)
                .orElseThrow(() -> new TransferServiceException(String.format("Account not found, provided accountNumber: %s, sortCode: %s", accountNumber, sortCode)));
    }

    private void validateAccountNumberAndSortCode(String accountNumber, String sortCode) {
        Optional.ofNullable(accountNumber)
                .filter(a -> StringUtils.isNotBlank(accountNumber) && accountNumber.matches(ACCOUNT_NUMBER_REGEX))
                .orElseThrow(() -> new TransferServiceException(String.format("Invalid accountNumber, %s. It must be 8 digits and number only.", accountNumber)));

        Optional.ofNullable(sortCode)
                .filter(s -> StringUtils.isNotBlank(s) && s.matches(SORT_CODE_REGEX))
                .orElseThrow(() -> new TransferServiceException(String.format("Invalid sortCode, %s. It must be 6 digits and number only.", sortCode)));
    }

    private void updateAccounts(Account... accounts) {
        Arrays.asList(accounts).forEach(account -> accountDao.updateAccount(account));
    }
}
