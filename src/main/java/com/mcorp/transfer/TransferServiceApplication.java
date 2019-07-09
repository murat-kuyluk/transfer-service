package com.mcorp.transfer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mcorp.transfer.controller.TransferController;
import com.mcorp.transfer.modules.DaoModule;
import com.mcorp.transfer.modules.PersistenceInitializer;
import com.mcorp.transfer.modules.PersistenceModule;
import com.mcorp.transfer.modules.ServiceModule;

import javax.inject.Inject;

import static spark.Spark.port;

public class TransferServiceApplication {

    private final TransferController controller;

    private final PersistenceInitializer persistenceInitializer;

    @Inject
    public TransferServiceApplication(TransferController controller, PersistenceInitializer persistenceInitializer) {
        this.controller = controller;
        this.persistenceInitializer = persistenceInitializer;
    }

    void run(int port) {
        port(port);
        persistenceInitializer.startPersistService();
        controller.initializeRoutes();
    }

    public static void main(String args[]) {

        Injector injector = Guice.createInjector(new PersistenceModule(), new ServiceModule(), new DaoModule());
        injector.getInstance(TransferServiceApplication.class).run(4567);
    }
}
