package com.mcorp.transfer.service;

import com.mcorp.transfer.model.TransferDto;
import com.mcorp.transfer.model.TransferRequest;


public interface TransferService {

    Long transfer(TransferRequest transferRequest);

    TransferDto retrieveTransferById(Long id);
}
