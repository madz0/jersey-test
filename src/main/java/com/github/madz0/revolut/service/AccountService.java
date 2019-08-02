package com.github.madz0.revolut.service;

import com.github.madz0.revolut.exception.DataIntegrityException;
import com.github.madz0.revolut.exception.ExternalServiceException;
import com.github.madz0.revolut.model.Account;
import com.github.madz0.revolut.model.BaseModel;
import com.github.madz0.revolut.model.Transfer;
import com.github.madz0.revolut.repository.AccountRepository;
import com.github.madz0.revolut.repository.Page;
import com.github.madz0.revolut.repository.TransferRepository;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;

import static com.github.madz0.revolut.util.CurrencyUtils.getDigitsCount;
import static com.github.madz0.revolut.util.CurrencyUtils.getFractionsCount;

@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final EntityManager entityManager;
    private final CurrencyService currencyService;
    private final TransferRepository transferRepository;

    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new DataIntegrityException("Could not find id " + id));
    }

    public Account save(Account account) {
        assertBigDecimalWithAllocatedSize(account.getAmount());
        return accountRepository.save(account);
    }

    public Page<Account> findAll(int page, int pageSize) {
        return accountRepository.findAll(page, pageSize);
    }

    public Transfer makeTransfer(Transfer transfer) {
        assertTransferIllegalFields(transfer);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            Account from = accountRepository.findForUpdateById(transfer.getFromAccountId()).orElseThrow(() -> new IllegalArgumentException("transfer from id " + transfer.getFromAccountId() + " was invalid"));
            Account to = accountRepository.findForUpdateById(transfer.getToAccountId()).orElseThrow(() -> new IllegalArgumentException("transfer destination id " + transfer.getToAccountId() + " was invalid"));
            //Check if from's account is sufficient for requested amount of transfer
            if (from.getAmount().compareTo(transfer.getAmount()) < 0) {
                throw new IllegalArgumentException("Insufficient amount in account " + from.getId());
            }
            BigDecimal exchangeRate = BigDecimal.ONE;
            if (from.getCurrency() != to.getCurrency()) {
                try {
                    exchangeRate = currencyService.getExchangeRateOf(from.getCurrency(), to.getCurrency());
                } catch (Exception e) {
                    throw new ExternalServiceException("Currency service call exception converting " + from.getCurrency() + ", to " + to.getCurrency(), e);
                }
            }
            from.setAmount(from.getAmount().subtract(transfer.getAmount()));
            to.setAmount(to.getAmount().add(transfer.getAmount().multiply(exchangeRate)));
            Transfer dbTransfer = transferRepository.save(prepareTransferForSave(transfer, from, to, exchangeRate));
            transaction.commit();
            return dbTransfer;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    private Transfer prepareTransferForSave(Transfer transfer, Account from, Account to, BigDecimal exchangeRate) {
        //Just to make sure!
        transfer.setId(null);
        transfer.setFrom(from);
        transfer.setTo(to);
        transfer.setFromCurrency(from.getCurrency());
        transfer.setToCurrency(to.getCurrency());
        transfer.setExchangeRate(exchangeRate);
        return transfer;
    }

    private void assertTransferIllegalFields(Transfer transfer) {
        if (transfer == null) {
            throw new IllegalArgumentException("transfer was null");
        }
        if (transfer.getFromAccountId() == null || transfer.getFromAccountId() <= 0) {
            throw new IllegalArgumentException("transfer source was illegal");
        }
        if (transfer.getToAccountId() == null || transfer.getToAccountId() <= 0) {
            throw new IllegalArgumentException("transfer destination was illegal");
        }
        if (transfer.getToAccountId().equals(transfer.getFromAccountId())) {
            throw new IllegalArgumentException("transfer source and destination was the same");
        }
        if (transfer.getAmount() == null || transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("transfer amount must be bigger than 0");
        }
        assertBigDecimalWithAllocatedSize(transfer.getAmount());
    }

    private void assertBigDecimalWithAllocatedSize(BigDecimal bigDecimal) {
        if (getDigitsCount(bigDecimal) > BaseModel.MAX_SUPPORTED_MONEY) {
            throw new IllegalArgumentException("transfer amount is bigger than allocated size");
        }
        if (getFractionsCount(bigDecimal) > BaseModel.MAX_SUPPORTED_MONEY_FRACTION) {
            throw new IllegalArgumentException("transfer amount is bigger than allocated size");
        }
    }
}
