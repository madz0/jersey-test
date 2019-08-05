package com.github.madz0.revolut.service;

import com.github.madz0.revolut.AbstractUnitTest;
import com.github.madz0.revolut.exception.DataIntegrityException;
import com.github.madz0.revolut.exception.ExternalServiceException;
import com.github.madz0.revolut.exception.RestIllegalArgumentException;
import com.github.madz0.revolut.exception.RestUnsupportedOperationException;
import com.github.madz0.revolut.model.Account;
import com.github.madz0.revolut.model.BaseModel;
import com.github.madz0.revolut.model.Currency;
import com.github.madz0.revolut.model.Transfer;
import com.github.madz0.revolut.repository.AccountRepository;
import com.github.madz0.revolut.repository.AccountRepositoryImpl;
import com.github.madz0.revolut.repository.Page;
import com.github.madz0.revolut.repository.TransferRepository;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Mockito.*;

public class AccountServiceUnitTest extends AbstractUnitTest {
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

    @Test(expected = RestIllegalArgumentException.class)
    public void getAccountByIdWrongIdTest_shouldReceivesIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        accountService.findById(null);
    }

    @Test
    public void createTest() {
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
        Account savedAccount = accountService.create(account);
        assertNotNull(savedAccount);
        assertEquals(generatedId, (long) savedAccount.getId());
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void createNullEntityTest_shouldThrowsIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        accountService.create(null);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void createEntityWithNotNullIdTest_shouldThrowsIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        Account account = new Account();
        account.setId(1L);
        accountService.create(account);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void createEntityWithNotNullVersionTest_shouldThrowsIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        Account account = new Account();
        account.setVersion(1L);
        accountService.create(account);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void createEntityWithLargerMoneyThanWeSupported_shouldThrowsIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        Account account = new Account();
        StringBuilder number = new StringBuilder();
        for (int i = 0; i <= BaseModel.MAX_SUPPORTED_MONEY; i++) {
            number.append("9");
        }
        account.setAmount(new BigDecimal(number.toString()));
        accountService.create(account);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void createEntityWithNullCurrency_shouldThrowsIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        Account account = new Account();
        account.setAmount(new BigDecimal("1"));
        accountService.create(account);
    }

    @Test
    public void updateTest() {
        accountRepository = mock(AccountRepository.class);
        doAnswer(invocationOnMock -> {
            Account acc = invocationOnMock.getArgument(0);
            if (acc == null) {
                return invocationOnMock.callRealMethod();
            } else {
                return acc;
            }
        }).when(accountRepository).save(Mockito.any(Account.class));
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), null, null);
        final Account account = new Account();
        account.setId(1L);
        account.setAmount(BigDecimal.TEN);
        account.setCurrency(Currency.USD);
        account.setVersion(1L);
        doAnswer(invocationOnMock -> Optional.of(account)).when(accountRepository).findForUpdateById(eq(1L));
        Account savedAccount = accountService.update(account);
        assertNotNull(savedAccount);
    }

    @Test(expected = DataIntegrityException.class)
    public void updateTest_whenEntityDoesNotExist_throwsDataIntegrityException() {
        accountRepository = mock(AccountRepository.class);
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), null, null);
        final Account account = new Account();
        account.setId(1L);
        account.setAmount(BigDecimal.TEN);
        account.setCurrency(Currency.USD);
        account.setVersion(1L);
        doAnswer(invocationOnMock -> Optional.empty()).when(accountRepository).findForUpdateById(eq(account.getId()));
        Account savedAccount = accountService.update(account);
        assertNotNull(savedAccount);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void updateNullEntityTest_shouldThrowsIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        accountService.update(null);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void updateEntityWithBadIdTest_shouldThrowsIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        Account account = new Account();
        account.setId(-1L);
        accountService.update(account);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void updateEntityWithNullIdTest_shouldThrowsIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        Account account = new Account();
        accountService.update(account);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void updateEntityWithWrongVersionTest_shouldThrowsIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        Account account = new Account();
        account.setId(1L);
        accountService.update(account);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void updateEntityWithLargerMoneyThanWeSupported_shouldThrowsIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        Account account = new Account();
        account.setId(1L);
        account.setVersion(1L);
        StringBuilder number = new StringBuilder();
        for (int i = 0; i <= BaseModel.MAX_SUPPORTED_MONEY; i++) {
            number.append("9");
        }
        account.setAmount(new BigDecimal(number.toString()));
        accountService.update(account);
    }

    @Test
    public void updateEntity_whenCurrencyIsNull_shouldStillWorks() {
        accountRepository = mock(AccountRepository.class);
        accountRepository = mock(AccountRepository.class);
        doAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0);
            if (id == null || id <= 0 || (id != 1)) {
                return invocationOnMock.callRealMethod();
            } else {
                Account account = new Account();
                account.setAmount(BigDecimal.TEN);
                account.setCurrency(Currency.USD);
                account.setId(1L);
                return Optional.of(account);
            }
        }).when(accountRepository).findForUpdateById(Mockito.anyLong());
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), null, null);
        Account account = new Account();
        account.setId(1L);
        account.setVersion(1L);
        account.setCurrency(null);
        account.setAmount(new BigDecimal("1"));
        accountService.update(account);
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

    @Test(expected = RestIllegalArgumentException.class)
    public void findAllWithWrongPageSizeTest_shouldThrowIllegalArgException() {
        accountRepository = new AccountRepositoryImpl(null);
        accountService = new AccountService(accountRepository, null, null, null);
        accountService.findAll(0, -1);
    }

    @Test(expected = RestIllegalArgumentException.class)
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

    @Test(expected = RestIllegalArgumentException.class)
    public void makeTransferNullTransferObjectTest_shouldThrowsIllegalArg() {
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), currencyService, transferRepository);
        accountService.makeTransfer(null);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void makeTransferNullTransferSenderIdTest_shouldThrowsIllegalArg() {
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), currencyService, transferRepository);
        Account to = new Account();
        to.setId(2L);
        Transfer transfer = new Transfer(null, to, BigDecimal.TEN, Currency.USD, Currency.EUR, null);
        accountService.makeTransfer(transfer);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void makeTransferNullTransferReceiverIdTest_shouldThrowsIllegalArg() {
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), currencyService, transferRepository);
        Account from = new Account();
        from.setId(2L);
        Transfer transfer = new Transfer(from, null, BigDecimal.TEN, Currency.USD, Currency.EUR, null);
        accountService.makeTransfer(transfer);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void makeTransferFromAnAccountToItselfTest_shouldThrowsIllegalArg() {
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), currencyService, transferRepository);
        Account from = new Account();
        from.setId(2L);
        Account to = new Account();
        to.setId(2L);
        Transfer transfer = new Transfer(from, to, BigDecimal.TEN, Currency.USD, Currency.EUR, null);
        accountService.makeTransfer(transfer);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void makeTransferWithNullAmountTest_shouldThrowsIllegalArg() {
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), currencyService, transferRepository);
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        Transfer transfer = new Transfer(from, to, null, null, null, null);
        accountService.makeTransfer(transfer);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void makeTransferWithZeroAmountTest_shouldThrowsIllegalArg() {
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), currencyService, transferRepository);
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        Transfer transfer = new Transfer(from, to, BigDecimal.ZERO, null, null, null);
        accountService.makeTransfer(transfer);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void makeTransferWithSmallerThanZeroAmountTest_shouldThrowsIllegalArg() {
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), currencyService, transferRepository);
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        Transfer transfer = new Transfer(from, to, new BigDecimal("-1"), null, null, null);
        accountService.makeTransfer(transfer);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void makeTransferWithBiggerDigitsThanWeSupportTest_shouldThrowsIllegalArg() {
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), currencyService, transferRepository);
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        StringBuilder number = new StringBuilder();
        for (int i = 0; i <= BaseModel.MAX_SUPPORTED_MONEY; i++) {
            number.append("1");
        }
        Transfer transfer = new Transfer(from, to, new BigDecimal(number.toString()), null, null, null);
        accountService.makeTransfer(transfer);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void makeTransferWithBiggerFractionThanWeSupportTest_shouldThrowsIllegalArg() {
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), currencyService, transferRepository);
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        Transfer transfer = new Transfer(from, to, new BigDecimal("1.00001"), null, null, null);
        accountService.makeTransfer(transfer);
    }

    @Test(expected = DataIntegrityException.class)
    public void makeTransferWithInvalidSenderTest_shouldThrowsIllegalArg() {
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

        accountRepository = mock(AccountRepository.class);
        doReturn(Optional.empty()).when(accountRepository).findForUpdateById(eq(fromInDb.getId()));
        doReturn(Optional.of(toInDb)).when(accountRepository).findForUpdateById(eq(toInDb.getId()));

        Transfer transfer = new Transfer(from, to, BigDecimal.TEN, Currency.USD, Currency.EUR, null);
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), currencyService, transferRepository);
        accountService.makeTransfer(transfer);
    }

    @Test(expected = DataIntegrityException.class)
    public void makeTransferWithInvalidReceiverTest_shouldThrowsIllegalArg() {
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

        accountRepository = mock(AccountRepository.class);
        doReturn(Optional.of(fromInDb)).when(accountRepository).findForUpdateById(eq(fromInDb.getId()));
        doReturn(Optional.empty()).when(accountRepository).findForUpdateById(eq(toInDb.getId()));

        Transfer transfer = new Transfer(from, to, BigDecimal.TEN, Currency.USD, Currency.EUR, null);
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), currencyService, transferRepository);
        accountService.makeTransfer(transfer);
    }

    @Test(expected = RestIllegalArgumentException.class)
    public void makeTransferWithInsufficientMoneyInSenderAccountTest_shouldThrowsIllegalArg() {
        BigDecimal fromOriginalAmount = BigDecimal.ONE;
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

        accountRepository = mock(AccountRepository.class);
        doReturn(Optional.of(fromInDb)).when(accountRepository).findForUpdateById(eq(fromInDb.getId()));
        doReturn(Optional.of(toInDb)).when(accountRepository).findForUpdateById(eq(toInDb.getId()));

        Transfer transfer = new Transfer(from, to, BigDecimal.TEN, Currency.USD, Currency.EUR, null);
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), currencyService, transferRepository);
        accountService.makeTransfer(transfer);
    }

    @Test(expected = ExternalServiceException.class)
    public void makeTransferWithTooBigExchangeRateTest_shouldThrowExternalServiceException() {
        BigDecimal fromOriginalAmount = new BigDecimal(BigInteger.ONE);
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

        StringBuilder number = new StringBuilder();
        for (int i = 0; i <= Transfer.EXCHANGE_RATE_MAX_DIGITS; i++) {
            number.append("9");
        }

        BigDecimal exchangeRateFromUsdToEur = new BigDecimal(number.toString());
        currencyService = mock(CurrencyService.class);
        doReturn(exchangeRateFromUsdToEur).when(currencyService).getExchangeRateOf(eq(Currency.USD), eq(Currency.EUR));

        accountRepository = mock(AccountRepository.class);
        doReturn(Optional.of(fromInDb)).when(accountRepository).findForUpdateById(eq(fromInDb.getId()));
        doReturn(Optional.of(toInDb)).when(accountRepository).findForUpdateById(eq(toInDb.getId()));

        Transfer transfer = new Transfer(from, to, new BigDecimal("1"), Currency.USD, Currency.EUR, null);
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), currencyService, transferRepository);
        accountService.makeTransfer(transfer);
    }

    @Test(expected = RestUnsupportedOperationException.class)
    public void makeTransferWithBigAmountAccountTest_shouldThrowUnsupportedOperation() {
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < BaseModel.MAX_SUPPORTED_MONEY + BaseModel.SUPPORTED_MONEY_SAFE_GUARD; i++) {
            number.append("9");
        }
        BigDecimal fromOriginalAmount = new BigDecimal(number.toString());
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

        number = new StringBuilder();
        for (int i = 0; i < Transfer.EXCHANGE_RATE_MAX_DIGITS; i++) {
            number.append("9");
        }

        BigDecimal exchangeRateFromUsdToEur = new BigDecimal(number.toString());
        currencyService = mock(CurrencyService.class);
        doReturn(exchangeRateFromUsdToEur).when(currencyService).getExchangeRateOf(eq(Currency.USD), eq(Currency.EUR));

        accountRepository = mock(AccountRepository.class);
        doReturn(Optional.of(fromInDb)).when(accountRepository).findForUpdateById(eq(fromInDb.getId()));
        doReturn(Optional.of(toInDb)).when(accountRepository).findForUpdateById(eq(toInDb.getId()));

        number = new StringBuilder();
        for (int i = 0; i < BaseModel.MAX_SUPPORTED_MONEY; i++) {
            number.append("9");
        }

        Transfer transfer = new Transfer(from, to, new BigDecimal(number.toString()), Currency.USD, Currency.EUR, null);
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), currencyService, transferRepository);
        accountService.makeTransfer(transfer);
    }

    @Test(expected = ExternalServiceException.class)
    public void makeTransferWhenExternalServiceFailsTest_shouldThrowExternalServiceException() {
        BigDecimal fromOriginalAmount = new BigDecimal("10");
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

        currencyService = mock(CurrencyService.class);
        doThrow(RuntimeException.class).when(currencyService).getExchangeRateOf(eq(Currency.USD), eq(Currency.EUR));

        accountRepository = mock(AccountRepository.class);
        doReturn(Optional.of(fromInDb)).when(accountRepository).findForUpdateById(eq(fromInDb.getId()));
        doReturn(Optional.of(toInDb)).when(accountRepository).findForUpdateById(eq(toInDb.getId()));

        Transfer transfer = new Transfer(from, to, new BigDecimal("2"), Currency.USD, Currency.EUR, null);
        accountService = new AccountService(accountRepository, mockEntityManagerTransaction(), currencyService, transferRepository);
        accountService.makeTransfer(transfer);
    }

    @Test
    public void findTransferByIdTest() {
        final long generatedAccountId = 1;
        final long generatedTransferId = 1;
        transferRepository = mock(TransferRepository.class);
        doAnswer(invocationOnMock -> {
            Long accountId = invocationOnMock.getArgument(0);
            Long transferId = invocationOnMock.getArgument(1);
            if (transferId == null || transferId <= 0 || accountId == null || accountId <= 0) {
                return invocationOnMock.callRealMethod();
            } else {
                Account account1 = new Account();
                account1.setAmount(BigDecimal.TEN);
                account1.setCurrency(Currency.USD);
                account1.setId(generatedAccountId);
                Account account2 = new Account();
                account2.setAmount(BigDecimal.TEN);
                account2.setCurrency(Currency.EUR);
                account2.setId(2L);
                Transfer transfer1 = new Transfer(account1, account2, BigDecimal.TEN, account1.getCurrency(), account2.getCurrency(), BigDecimal.ONE);
                transfer1.setId(generatedTransferId);
                return Optional.of(transfer1);
            }
        }).when(transferRepository).findTransferByAccountIdAndId(Mockito.anyLong(), anyLong());
        accountService = new AccountService(accountRepository, null, null, transferRepository);
        Transfer savedTransfer = accountService.findTransferById(generatedAccountId, generatedTransferId);
        assertNotNull(savedTransfer);
        assertEquals(generatedTransferId, (long) savedTransfer.getId());
    }

    @Test
    public void findAllTransfersByAccountIdTest() {
        final long account1Id = 1;
        final long account2Id = 2;
        final long account3Id = 3;
        transferRepository = mock(TransferRepository.class);
        doAnswer(invocationOnMock -> {

            long accountId = invocationOnMock.getArgument(0);
            int page = invocationOnMock.getArgument(1);
            int size = invocationOnMock.getArgument(2);
            if (page < 0 || size <= 0) {
                return invocationOnMock.callRealMethod();
            } else {
                Account account1 = new Account();
                account1.setAmount(BigDecimal.TEN);
                account1.setCurrency(Currency.USD);
                account1.setId(account1Id);
                Account account2 = new Account();
                account2.setAmount(BigDecimal.TEN);
                account2.setCurrency(Currency.EUR);
                account2.setId(account2Id);
                Account account3 = new Account();
                account3.setAmount(BigDecimal.TEN);
                account3.setCurrency(Currency.EUR);
                account3.setId(account3Id);

                Transfer transfer1 = new Transfer(account1, account2, BigDecimal.TEN, account1.getCurrency(), account2.getCurrency(), BigDecimal.ONE);
                transfer1.setId(1L);
                Transfer transfer2 = new Transfer(account1, account3, BigDecimal.ONE, account1.getCurrency(), account3.getCurrency(), BigDecimal.ONE);
                transfer2.setId(2L);
                Transfer transfer3 = new Transfer(account2, account3, BigDecimal.ONE, account2.getCurrency(), account3.getCurrency(), BigDecimal.ONE.add(BigDecimal.TEN));
                transfer3.setId(3L);
                List<Transfer> transferList = Stream.of(transfer1, transfer2, transfer3).filter(t -> t.getFromAccountId() == accountId || t.getToAccountId() == accountId).collect(Collectors.toList());
                return new Page<>(transferList.subList(page, page + size), transferList.size(), page, size);
            }
        }).when(transferRepository).findAllByAccountId(or(eq(account1Id), eq(account2Id)), anyInt(), anyInt());
        accountService = new AccountService(accountRepository, null, null, transferRepository);
        int page = 0;
        int pageSize = 2;
        Page<Transfer> transferPage = accountService.findAllTransfersByAccountId(1L, page, pageSize);
        assertNotNull(transferPage);
        assertEquals(page, transferPage.getPage());
        assertEquals(pageSize, transferPage.getPageSize());
        assertEquals(2, transferPage.getTotalSize());
        assertEquals(pageSize, transferPage.getContents().size());
        assertEquals(1, (long) transferPage.getContents().get(0).getId());
    }

    @Test
    public void findAllTransfersByAccountIdEmptyResultTest() {
        transferRepository = mock(TransferRepository.class);
        doAnswer(invocationOnMock -> {
            int page = invocationOnMock.getArgument(1);
            int size = invocationOnMock.getArgument(2);
            if (page < 0 || size <= 0) {
                return invocationOnMock.callRealMethod();
            } else {
                return new Page<Transfer>(Collections.emptyList(), 0, page, size);
            }
        }).when(transferRepository).findAllByAccountId(anyLong(), anyInt(), anyInt());
        accountService = new AccountService(accountRepository, null, null, transferRepository);
        int page = 0;
        int pageSize = 2;
        Page<Transfer> transferPage = accountService.findAllTransfersByAccountId(1L, page, pageSize);
        assertNotNull(transferPage);
        assertEquals(0, transferPage.getPage());
        assertEquals(pageSize, transferPage.getPageSize());
        assertEquals(0, transferPage.getTotalSize());
        assertEquals(0, transferPage.getContents().size());
        assertEquals(0, (long) transferPage.getContents().size());
    }
}
