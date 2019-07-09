package com.mcorp.transfer.dao.impl;

import com.mcorp.transfer.dao.TransferDao;
import com.mcorp.transfer.dao.entity.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.Optional;

@Singleton
public class TransferDaoImpl implements TransferDao {

    private static final Logger logger = LoggerFactory.getLogger(TransferDaoImpl.class);

    private final Provider<EntityManager> entityManagerProvider;

    @Inject
    public TransferDaoImpl(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    public void save(Transfer transfer) {
        logger.debug("Transfer save starts, {}", transfer);
        entityManagerProvider.get().persist(transfer);
        logger.debug("Transfer save ends, {}", transfer);
    }

    @Override
    public Optional<Transfer> retrieveTransferById(Long id) {
        return Optional.ofNullable(entityManagerProvider.get().find(Transfer.class, id));
    }
}
