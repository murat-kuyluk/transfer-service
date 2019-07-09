package com.mcorp.transfer.service;

import com.mcorp.transfer.dao.TransferDao;
import com.mcorp.transfer.dao.entity.Transfer;
import com.mcorp.transfer.model.TransferDto;
import com.mcorp.transfer.model.TransferRequest;
import com.mcorp.transfer.service.impl.TransferServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static com.mcorp.transfer.TestDataHelper.*;
import static com.mcorp.transfer.model.AccountDto.AccountDtoBuilder.anAccountDto;
import static com.mcorp.transfer.model.TransferRequest.TransferRequestBuilder.aTransferRequest;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransferServiceTest {

    private TransferServiceImpl transferService;

    @Mock
    private TransferDao dao;

    @Mock
    private AccountService accountService;

    @Before
    public void setUp() {
        transferService = new TransferServiceImpl(dao, accountService);
    }

    @Test
    public void transfer_shouldUpdateFromToAccountsBalance() {

        TransferRequest transferRequest = aTransferRequest()
                .withAmount(BigDecimal.valueOf(150.47))
                .withFrom(anAccountDto().withNumber("70328725").withSortCode("080054").build())
                .withNote("Automated transfer")
                .withTo(anAccountDto().withNumber("00813796").withSortCode("800551").build())
                .build();

        transferService.transfer(transferRequest);

        verify(accountService).updateBalance(transferRequest.getFrom(), transferRequest.getTo(), transferRequest.getAmount());
        verify(dao).save(aTransfer());
    }

    @Test
    public void retrieveTransferById_shouldReturnTransferDetails() {

        Transfer transfer = aTransfer();
        transfer.setId(Long.valueOf(1));
        when(dao.retrieveTransferById(anyLong())).thenReturn(Optional.of(transfer));

        TransferDto actual = transferService.retrieveTransferById(Long.valueOf(1));

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(aTransferDto());
    }
}