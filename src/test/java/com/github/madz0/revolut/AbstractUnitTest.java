package com.github.madz0.revolut;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.mockito.Mockito.*;

public class AbstractUnitTest {
    protected EntityManager mockEntityManagerTransaction() {
        EntityTransaction transaction = mock(EntityTransaction.class);
        doNothing().when(transaction).begin();
        doNothing().when(transaction).commit();
        doNothing().when(transaction).rollback();
        EntityManager entityManager = mock(EntityManager.class);
        doReturn(transaction).when(entityManager).getTransaction();
        return entityManager;
    }
}
