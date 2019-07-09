package com.mcorp.transfer;

import com.mcorp.transfer.dao.entity.Account;
import com.mcorp.transfer.dao.entity.Transfer;
import com.mcorp.transfer.model.TransferDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class TestDataHelper {

    public static Account anAccount(String accountNumber, String sortCode, BigDecimal balance){
        return getAccount(Long.valueOf("1"), accountNumber, sortCode, balance);
    }

    public static Account getAccount(Long id, String accountNumber, String sortCode, BigDecimal balance) {
        Account account = new Account();
        account.setId(id);
        account.setBalance(balance);
        account.setAccountNumber(accountNumber);
        account.setSortCode(sortCode);
        account.setOwnerFullName("Test User");
        Instant instant = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        Date from = Date.from(instant);
        account.setCreateDate(from);
        account.setModifyDate(from);
        return account;
    }

    public static Transfer aTransfer(){
        return new Transfer("080054 | 70328725", "800551 | 00813796", "Automated transfer", BigDecimal.valueOf(150.47));
    }

    public static TransferDto aTransferDto(){
        return new TransferDto(Long.valueOf(1),"080054 | 70328725", "800551 | 00813796",  BigDecimal.valueOf(150.47), "Automated transfer");
    }
}
