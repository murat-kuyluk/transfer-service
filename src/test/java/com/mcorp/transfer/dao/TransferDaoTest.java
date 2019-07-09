package com.mcorp.transfer.dao;

import com.mcorp.transfer.TestDataHelper;
import com.mcorp.transfer.dao.entity.Transfer;
import com.mcorp.transfer.dao.impl.TransferDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.inject.Provider;
import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransferDaoTest {

    private TransferDao dao;

    @Mock
    private Provider<EntityManager> entityManagerProvider;

    @Mock
    private EntityManager em;

    @Before
    public void setUp() {
        dao = new TransferDaoImpl(entityManagerProvider);
        when(entityManagerProvider.get()).thenReturn(em);
    }

    @Test
    public void save_shouldPersistTransfer() {

        doAnswer(invocation -> {
            Transfer argument = invocation.getArgument(0);
            argument.setId(Long.valueOf(1));
            return null;
        }).when(em).persist(any(Transfer.class));

        Transfer transfer = TestDataHelper.aTransfer();
        dao.save(transfer);

        verify(em).persist(transfer);
        assertThat(transfer.getId()).isEqualTo(Long.valueOf(1));
    }

    @Test
    public void retrieveTransferById_shouldReturnTransfer() {

        Transfer transfer = TestDataHelper.aTransfer();
        when(em.find(eq(Transfer.class), anyLong())).thenReturn(transfer);

        Optional<Transfer> actual = dao.retrieveTransferById(Long.valueOf(1));

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(transfer);
        verify(em).find(Transfer.class, Long.valueOf(1));
    }
}