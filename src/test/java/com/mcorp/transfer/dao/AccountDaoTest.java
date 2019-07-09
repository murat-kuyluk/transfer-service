package com.mcorp.transfer.dao;

import com.mcorp.transfer.TestDataHelper;
import com.mcorp.transfer.dao.entity.Account;
import com.mcorp.transfer.dao.impl.AccountDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountDaoTest {

    private AccountDao dao;

    @Mock
    private Provider<EntityManager> entityManagerProvider;

    @Mock
    private EntityManager em;

    @Before
    public void setUp() {
        dao = new AccountDaoImpl(entityManagerProvider);
        when(entityManagerProvider.get()).thenReturn(em);
    }

    @Test
    public void updateAccount_shouldMergeAccountEntity() {

        Date modifyDate = new Date();
        doAnswer(invocation -> {
            Account argument = invocation.getArgument(0);
            argument.setVersion(argument.getVersion() + 1);
            argument.setModifyDate(modifyDate);
            return null;
        }).when(em).merge(any(Account.class));

        Account account = new Account();
        account.setVersion(1);
        dao.updateAccount(account);

        verify(em).merge(account);
        assertThat(account.getModifyDate()).isNotNull();
        assertThat(account.getModifyDate()).isEqualTo(modifyDate);
        assertThat(account.getVersion()).isEqualTo(2);
    }

    @Test
    public void findAccountByNumberAndSortCode_shouldRetrieveAccount() {

        TypedQuery tq = mock(TypedQuery.class);
        when(em.createNamedQuery(anyString(), eq(Account.class))).thenReturn(tq);
        Account expectedAccount = TestDataHelper.anAccount("09876091", "010203", BigDecimal.valueOf(129.99));
        when(tq.getSingleResult()).thenReturn(expectedAccount);

        Optional<Account> account = dao.findAccountByNumberAndSortCode("09876091", "010203");

        verify(tq).setParameter("accountNumber", "09876091");
        verify(tq).setParameter("sortCode", "010203");

        assertThat(account.get()).isEqualTo(expectedAccount);
    }

    @Test
    public void findAccountByNumberAndSortCode_shouldReturnEmptyOptional() {

        TypedQuery tq = mock(TypedQuery.class);
        when(em.createNamedQuery(anyString(), eq(Account.class))).thenReturn(tq);
        when(tq.getSingleResult()).thenThrow(new NoResultException("No result."));

        Optional<Account> account = dao.findAccountByNumberAndSortCode("09876091", "010203");

        verify(tq).setParameter("accountNumber", "09876091");
        verify(tq).setParameter("sortCode", "010203");

        assertThat(account.isPresent()).isFalse();
    }
}