package com.mcorp.transfer.modules;

import com.google.inject.AbstractModule;
import com.mcorp.transfer.service.AccountService;
import com.mcorp.transfer.service.impl.AccountServiceImpl;
import com.mcorp.transfer.service.TransferService;
import com.mcorp.transfer.service.impl.TransferServiceImpl;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TransferService.class).to(TransferServiceImpl.class);
        bind(AccountService.class).to(AccountServiceImpl.class);
    }
}
