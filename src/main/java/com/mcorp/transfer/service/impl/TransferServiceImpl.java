package com.mcorp.transfer.service.impl;


import com.google.inject.persist.Transactional;
import com.mcorp.transfer.dao.TransferDao;
import com.mcorp.transfer.dao.entity.Transfer;
import com.mcorp.transfer.exception.TransferServiceException;
import com.mcorp.transfer.model.AccountDto;
import com.mcorp.transfer.model.TransferDto;
import com.mcorp.transfer.model.TransferRequest;
import com.mcorp.transfer.service.AccountService;
import com.mcorp.transfer.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class TransferServiceImpl implements TransferService {

    private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

    private final TransferDao transferDao;
    private final AccountService accountService;

    @Inject
    public TransferServiceImpl(TransferDao transferDao, AccountService accountService) {
        this.transferDao = transferDao;
        this.accountService = accountService;
    }

    @Transactional
    @Override
    public Long transfer(TransferRequest transferRequest) {
        logger.debug("transfer starts with parameters {}", transferRequest);
        accountService.updateBalance(transferRequest.getFrom(), transferRequest.getTo(), transferRequest.getAmount());

        Transfer transfer = mapToTransfer(transferRequest);
        transferDao.save(transfer);

        logger.debug("transfer ends successfully {}", transfer.getId());
        return transfer.getId();
    }

    @Override
    public TransferDto retrieveTransferById(Long id) {
        Transfer transfer = transferDao.retrieveTransferById(id)
                .orElseThrow(() -> new TransferServiceException(String.format("Transfer not found for given id %s", id.toString())));
        return mapToTransferDto(transfer);
    }

    private TransferDto mapToTransferDto(Transfer transfer) {
        return new TransferDto(transfer.getId(), transfer.getFromAccount(), transfer.getToAccount(), transfer.getAmount(), transfer.getReference());
    }

    private Transfer mapToTransfer(TransferRequest dto) {
        String from = concatAccountNameSortCode(dto.getFrom());
        String to = concatAccountNameSortCode(dto.getTo());
        Transfer transfer = new Transfer(from, to, dto.getNote(), dto.getAmount());
        return transfer;
    }

    private String concatAccountNameSortCode(AccountDto dto) {
        return dto.getSortCode().concat(" | ").concat(dto.getNumber());
    }
}
