package com.mcorp.transfer.modules;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

public class PersistenceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JpaPersistModule("db-manager"));
        bind(PersistenceInitializer.class);
    }
}
