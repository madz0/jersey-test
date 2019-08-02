package com.github.madz0.revolut.service;

import com.github.madz0.revolut.model.Account;
import com.github.madz0.revolut.model.Currency;
import com.github.madz0.revolut.model.Transfer;
import com.github.madz0.revolut.repository.AccountRepository;
import com.github.madz0.revolut.repository.AccountRepositoryImpl;
import com.github.madz0.revolut.repository.Page;
import com.github.madz0.revolut.repository.TransferRepository;
import org.junit.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    private AccountService accountService;
    private AccountRepository accountRepository;
    private CurrencyService currencyService;
    private TransferRepository transferRepository;

    @Test
    public void getAccountByIdTest() {
        final long generatedId = 1;
        accountRepository = mock(AccountRepository.class);
        doAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0);
            if (id == null || id <= 0 || (id != generatedId)) {
                return invocationOnMock.callRealMethod();
            } else {
                Account account = new Account();
                account.setAmount(BigDecimal.TEN);
                account.setCurrency(Currency.USD);
                account.setId(generatedId);
                return Optional.of(account);
            }
        }).when(accountRepository).findById(Mockito.anyLong());
        accountService = new AccountService(accountRepository, null, null, null);
        Account savedAccount = accountService.findById(generatedId);
        assertNotNull(savedAccount);
        assertEquals(generatedId, (long) savedAccount.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAccountByIdWrongIdTest_shouldReceivesIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        accountService.findById(null);
    }

    @Test
    public void saveTest() {
        final long generatedId = 1;
        accountRepository = mock(AccountRepository.class);
        doAnswer(invocationOnMock -> {
            Account acc = invocationOnMock.getArgument(0);
            if (acc == null) {
                return invocationOnMock.callRealMethod();
            } else {
                acc.setId(generatedId);
                return acc;
            }
        }).when(accountRepository).save(Mockito.any(Account.class));
        accountService = new AccountService(accountRepository, null, null, null);
        Account account = new Account();
        account.setAmount(BigDecimal.TEN);
        account.setCurrency(Currency.USD);
        Account savedAccount = accountService.save(account);
        assertNotNull(savedAccount);
        assertEquals(generatedId, (long) savedAccount.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveNullEntityTest_shouldThrowsIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        accountService.save(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveEntityWithWrongIdTest_shouldThrowsIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        accountService.save(null);
    }

    @Test
    public void findAllTest() {
        accountRepository = mock(AccountRepository.class);
        doAnswer(invocationOnMock -> {
            int page = invocationOnMock.getArgument(0);
            int size = invocationOnMock.getArgument(1);
            if (page < 0 || size <= 0) {
                return invocationOnMock.callRealMethod();
            } else {
                Account account1 = new Account();
                account1.setAmount(BigDecimal.TEN);
                account1.setCurrency(Currency.USD);
                account1.setId(1L);
                Account account2 = new Account();
                account2.setAmount(BigDecimal.TEN);
                account2.setCurrency(Currency.EUR);
                account2.setId(2L);
                Account account3 = new Account();
                account3.setAmount(BigDecimal.TEN);
                account3.setCurrency(Currency.EUR);
                account3.setId(3L);
                List<Account> accountList = Arrays.asList(account1, account2, account3);
                return new Page<>(accountList.subList(page, page + size), accountList.size(), page, size);
            }
        }).when(accountRepository).findAll(anyInt(), anyInt());
        accountService = new AccountService(accountRepository, null, null, null);
        int page = 0;
        int pageSize = 2;
        Page<Account> accountPage = accountService.findAll(page, pageSize);
        assertNotNull(accountPage);
        assertEquals(page, accountPage.getPage());
        assertEquals(pageSize, accountPage.getPageSize());
        assertEquals(pageSize, accountPage.getPageSize());
        assertEquals(3, accountPage.getTotalSize());
        assertEquals(pageSize, accountPage.getContents().size());
        assertEquals(1, (long) accountPage.getContents().get(0).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findAllWithWrongPageSizeTest_shouldThrowIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        accountService.findAll(0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findAllWithWrongPageTest_shouldThrowIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        accountService.findAll(-1, 1);
    }

    @Test
    public void makeTransferTest() {
        BigDecimal fromOriginalAmount = BigDecimal.TEN;
        BigDecimal toOriginalAmount = BigDecimal.TEN;
        final Account fromInDb = new Account();
        fromInDb.setId(1L);
        fromInDb.setAmount(fromOriginalAmount);
        fromInDb.setCurrency(Currency.USD);

        final Account toInDb = new Account();
        toInDb.setId(2L);
        toInDb.setAmount(toOriginalAmount);
        toInDb.setCurrency(Currency.EUR);

        Account from = new Account();
        from.setId(fromInDb.getId());
        Account to = new Account();
        to.setId(toInDb.getId());

        BigDecimal exchangeRateFromUsdToEur = new BigDecimal("2.01");
        currencyService = mock(CurrencyService.class);
        doReturn(exchangeRateFromUsdToEur).when(currencyService).getExchangeRateOf(eq(Currency.USD), eq(Currency.EUR));

        accountRepository = mock(AccountRepository.class);
        doReturn(Optional.of(fromInDb)).when(accountRepository).findForUpdateById(eq(fromInDb.getId()));
        doReturn(Optional.of(toInDb)).when(accountRepository).findForUpdateById(eq(toInDb.getId()));

        transferRepository = mock(TransferRepository.class);
        doAnswer(invocationOnMock -> {
            Transfer transfer = invocationOnMock.getArgument(0);
            transfer.setId(1L);
            return transfer;
        }).when(transferRepository).save(any(Transfer.class));

        Transfer transfer = new Transfer(from, to, BigDecimal.TEN, Currency.USD, Currency.EUR, null);

        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), currencyService, transferRepository);

        Transfer returnedTransfer = accountService.makeTransfer(transfer);
        assertNotNull(returnedTransfer);
        assertNotNull(returnedTransfer.getId());
        assertEquals(from.getId(), returnedTransfer.getFromAccountId());
        assertNotNull(returnedTransfer.getFrom());
        assertNotNull(returnedTransfer.getTo());
        assertEquals(to.getId(), returnedTransfer.getToAccountId());
        assertEquals(transfer.getAmount(), returnedTransfer.getAmount());
        assertEquals("amount of subtraction for sender should be correct", fromOriginalAmount.subtract(transfer.getAmount()), fromInDb.getAmount());
        assertEquals("amount of added amount for receiver should be correct", toOriginalAmount.add(transfer.getAmount().multiply(exchangeRateFromUsdToEur)), toInDb.getAmount());
    }

    private EntityManager mockEntityManagerTransaction() {
        EntityTransaction transaction = mock(EntityTransaction.class);
        doNothing().when(transaction).commit();
        EntityManager entityManager = mock(EntityManager.class);
        doReturn(transaction).when(entityManager).getTransaction();
        return entityManager;
    }
}
