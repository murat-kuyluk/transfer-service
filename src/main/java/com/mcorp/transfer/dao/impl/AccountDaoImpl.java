package com.mcorp.transfer.dao.impl;

import com.google.inject.persist.Transactional;
import com.mcorp.transfer.dao.AccountDao;
import com.mcorp.transfer.dao.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Singleton
public class AccountDaoImpl implements AccountDao {

    private static final Logger logger = LoggerFactory.getLogger(AccountDaoImpl.class);

    private final Provider<EntityManager> entityManagerProvider;

    @Inject
    public AccountDaoImpl(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    public void updateAccount(Account account) {
        logger.debug("updateAccount starts with params : {}", account);
        entityManagerProvider.get().merge(account);
        logger.debug("updateAccount ends.");
    }

    @Override
    public Optional<Account> findAccountByNumberAndSortCode(String accountNumber, String sortCode) {
        logger.debug("findAccountByNumberAndSortCode starts with params {}, {}", accountNumber, sortCode);
        TypedQuery<Account> accountTypedQuery = entityManagerProvider.get().createNamedQuery("Account.findAccountByNumberAndSortCode", Account.class);
        accountTypedQuery.setParameter("accountNumber", accountNumber);
        accountTypedQuery.setParameter("sortCode", sortCode);
        try {
            Account result = accountTypedQuery.getSingleResult();
            logger.debug("findAccountByNumberAndSortCode ends, account: {}", result);
            return Optional.of(result);
        } catch (PersistenceException ex) {
            logger.error("Error occurred while querying account, accountNumber {}, sortCode {}, exception: ", accountNumber, sortCode, ex);
        }
        return Optional.empty();
    }

    //this method is for BDD tests to provision accounts.
    @Override
    @Transactional
    public void save(Account account){
        entityManagerProvider.get().persist(account);
    }
}
