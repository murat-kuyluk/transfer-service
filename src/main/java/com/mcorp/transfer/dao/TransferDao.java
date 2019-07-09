package com.mcorp.transfer.dao;

import com.mcorp.transfer.dao.entity.Transfer;

import java.util.Optional;

public interface TransferDao {

    void save(Transfer transfer);

    Optional<Transfer> retrieveTransferById(Long id);
}
