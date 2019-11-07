package com.github.madz0.jerseytest.service;

import com.github.madz0.jerseytest.model.Account;
import com.github.madz0.jerseytest.model.Currency;
import com.github.madz0.jerseytest.model.Transfer;
import com.github.madz0.jerseytest.repository.AccountRepository;
import com.github.madz0.jerseytest.repository.AccountRepositoryImpl;
import com.github.madz0.jerseytest.repository.TransferRepository;
import com.github.madz0.jerseytest.repository.TransferRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@Slf4j
public class AccountServiceWithRealEntityManagerIntegrationTest {
    private static EntityManagerFactory entityManagerFactory;

    @Mock
    private CurrencyService currencyService;

    @BeforeAll
    public static void beforeClassSetup() {
        entityManagerFactory = Persistence.createEntityManagerFactory("h2-unit");
    }

    @BeforeEach
    public void beforeEachSetup() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    public void afterEach() {
    }

    @Test
    public void makeConcurrentTransferTest_shouldNotMessWithAccountsAmount() throws InterruptedException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        AccountRepository accountRepository = new AccountRepositoryImpl(entityManager);
        TransferRepository transferRepository = new TransferRepositoryImpl(entityManager);
        doReturn(BigDecimal.ONE).when(currencyService).getExchangeRateOf(any(), any());
        Account account1 = new Account();
        account1.setCurrency(Currency.USD);
        account1.setAmount(new BigDecimal("100.50"));
        Account account2 = new Account();
        account2.setCurrency(Currency.USD);
        account2.setAmount(new BigDecimal("50.50"));
        account1 = accountRepository.save(account1);
        account2 = accountRepository.save(account2);
        final Account account1Final = account1, account2Final = account2;

        CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread t1 = new Thread(() -> {
            try {
                EntityManager entityManager1 = entityManagerFactory.createEntityManager();
                AccountRepository accountRepository1 = new AccountRepositoryImpl(entityManager);
                TransferRepository transferRepository1 = new TransferRepositoryImpl(entityManager);
                AccountService accountService = new AccountService(accountRepository1, entityManager1, currencyService, transferRepository1);
                accountService.makeTransfer(new Transfer(account1Final, account2Final, new BigDecimal(25), null, null, null));
            } catch (Exception e) {
                log.error("1", e);
            }
            countDownLatch.countDown();
        });
        Thread t2 = new Thread(() -> {
            try {
                EntityManager entityManager1 = entityManagerFactory.createEntityManager();
                AccountRepository accountRepository1 = new AccountRepositoryImpl(entityManager);
                TransferRepository transferRepository1 = new TransferRepositoryImpl(entityManager);
                AccountService accountService = new AccountService(accountRepository1, entityManager1, currencyService, transferRepository1);
                accountService.makeTransfer(new Transfer(account1Final, account2Final, new BigDecimal(25), null, null, null));
            } catch (Exception e) {
                log.error("2", e);
            }
            countDownLatch.countDown();
        });
        t1.start();
        t2.start();
        countDownLatch.await();
        assertEquals(new BigDecimal("50.50"), accountRepository.findById(account1.getId()).get().getAmount());
        assertEquals(new BigDecimal("100.50"), accountRepository.findById(account1.getId()).get().getAmount());
    }
}
