package com.mcorp.transfer.modules;

import com.google.inject.AbstractModule;
import com.mcorp.transfer.dao.AccountDao;
import com.mcorp.transfer.dao.impl.AccountDaoImpl;
import com.mcorp.transfer.dao.TransferDao;
import com.mcorp.transfer.dao.impl.TransferDaoImpl;

public class DaoModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AccountDao.class).to(AccountDaoImpl.class);
        bind(TransferDao.class).to(TransferDaoImpl.class);
    }
}
