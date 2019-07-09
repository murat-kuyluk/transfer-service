package com.mcorp.transfer.modules;

import com.google.inject.persist.PersistService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PersistenceInitializer {

    private final PersistService service;

    @Inject
    public PersistenceInitializer(PersistService service){
        this.service = service;
    }

    public void startPersistService(){
        service.start();
    }

    public void stopPersistService(){
        service.stop();
    }
}
