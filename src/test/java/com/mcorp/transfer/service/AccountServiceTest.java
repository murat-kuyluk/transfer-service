package com.mcorp.transfer.service;

import com.mcorp.transfer.dao.AccountDao;
import com.mcorp.transfer.dao.entity.Account;
import com.mcorp.transfer.service.impl.AccountServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static com.mcorp.transfer.TestDataHelper.anAccount;
import static com.mcorp.transfer.model.AccountDto.AccountDtoBuilder.anAccountDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    public static final String FROM_ACCOUNT_NUMBER = "06071210";
    public static final String FROM_SORT_CODE = "121212";
    public static final String TO_SORT_CODE = "121213";
    public static final String TO_ACCOUNT_NUMBER = "06071310";

    private AccountService accountService;

    @Mock
    private AccountDao accountDao;

    @Before
    public void setUp() {
        accountService = new AccountServiceImpl(accountDao);
    }

    @Test
    public void updateBalance_shouldUpdateAccountsBalance() {

        Account fromAccount = anAccount(FROM_ACCOUNT_NUMBER, FROM_SORT_CODE, BigDecimal.valueOf(125.64));
        Account toAccount = anAccount(TO_ACCOUNT_NUMBER, TO_SORT_CODE, BigDecimal.valueOf(655.38));
        when(accountDao.findAccountByNumberAndSortCode(eq(FROM_ACCOUNT_NUMBER), eq(FROM_SORT_CODE))).thenReturn(Optional.of(fromAccount));
        when(accountDao.findAccountByNumberAndSortCode(eq(TO_ACCOUNT_NUMBER), eq(TO_SORT_CODE))).thenReturn(Optional.of(toAccount));

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        accountService.updateBalance(anAccountDto().withNumber(FROM_ACCOUNT_NUMBER).withSortCode(FROM_SORT_CODE).build(),
                anAccountDto().withNumber(TO_ACCOUNT_NUMBER).withSortCode(TO_SORT_CODE).build(), BigDecimal.valueOf(120.90));

        verify(accountDao,times(2)).updateAccount(captor.capture());

        assertThat(captor.getAllValues().get(0).getBalance()).isEqualTo(BigDecimal.valueOf(4.74));
        assertThat(captor.getAllValues().get(1).getBalance()).isEqualTo(BigDecimal.valueOf(776.28));
    }

    @Test
    public void updateBalance_shouldThrowException_whenFromAccountBalanceIsLow() {

        Optional<Account> fromAccount = Optional.ofNullable(anAccount(FROM_ACCOUNT_NUMBER, FROM_SORT_CODE, BigDecimal.valueOf(125.64)));
        when(accountDao.findAccountByNumberAndSortCode(eq(FROM_ACCOUNT_NUMBER), eq(FROM_SORT_CODE))).thenReturn(fromAccount);

        assertThatThrownBy(() -> accountService.updateBalance(anAccountDto().withNumber(FROM_ACCOUNT_NUMBER).withSortCode(FROM_SORT_CODE).build(),
                anAccountDto().withNumber(TO_ACCOUNT_NUMBER).withSortCode(TO_SORT_CODE).build(), BigDecimal.valueOf(125.65))
        ).isInstanceOf(RuntimeException.class)
                .hasMessage("From account has insufficient fund");

    }

    @Test
    public void updateBalance_shouldThrowException_whenFromAccountNotFound() {

        when(accountDao.findAccountByNumberAndSortCode(eq(FROM_ACCOUNT_NUMBER), eq(FROM_SORT_CODE))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.updateBalance(anAccountDto().withNumber(FROM_ACCOUNT_NUMBER).withSortCode(FROM_SORT_CODE).build(),
                anAccountDto().withNumber(TO_ACCOUNT_NUMBER).withSortCode(TO_SORT_CODE).build(), BigDecimal.valueOf(125.65))
        ).isInstanceOf(RuntimeException.class)
                .hasMessage("Account not found, provided accountNumber: 06071210, sortCode: 121212");

    }

    @Test
    public void updateBalance_shouldThrowException_whenAccountNumberIsInvalid() {
        assertThatThrownBy(() -> accountService.updateBalance(anAccountDto().withNumber("1234567").withSortCode(FROM_SORT_CODE).build(),
                anAccountDto().withNumber(TO_ACCOUNT_NUMBER).withSortCode(TO_SORT_CODE).build(), BigDecimal.valueOf(125.65))
        ).isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid accountNumber, 1234567. It must be 8 digits and number only.");
    }

    @Test
    public void updateBalance_shouldThrowException_whenSortCodeIsInvalid() {
        assertThatThrownBy(() -> accountService.updateBalance(anAccountDto().withNumber(FROM_ACCOUNT_NUMBER).withSortCode("01016").build(),
                anAccountDto().withNumber(TO_ACCOUNT_NUMBER).withSortCode(TO_SORT_CODE).build(), BigDecimal.valueOf(125.65))
        ).isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid sortCode, 01016. It must be 6 digits and number only.");
    }
}