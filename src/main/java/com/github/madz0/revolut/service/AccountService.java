package com.github.madz0.revolut.service;

import com.github.madz0.revolut.exception.DataIntegrityException;
import com.github.madz0.revolut.exception.ExternalServiceException;
import com.github.madz0.revolut.exception.RestIllegalArgumentException;
import com.github.madz0.revolut.exception.RestUnsupportedOperationException;
import com.github.madz0.revolut.model.Account;
import com.github.madz0.revolut.model.BaseModel;
import com.github.madz0.revolut.model.Transfer;
import com.github.madz0.revolut.repository.AccountRepository;
import com.github.madz0.revolut.repository.Page;
import com.github.madz0.revolut.repository.TransferRepository;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;

import static com.github.madz0.revolut.util.CurrencyUtils.getDigitsCount;
import static com.github.madz0.revolut.util.CurrencyUtils.getFractionsCount;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccountService {
    private final static int DEFAULT_PAGE_SIZE = 20;
    private final AccountRepository accountRepository;
    private final EntityManager entityManager;
    private final CurrencyService currencyService;
    private final TransferRepository transferRepository;

    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new DataIntegrityException(null, "Could not find id " + id));
    }

    public Account create(Account account) {
        assertAccountBeforeSave(account);
        assertBigDecimalWithAllocatedSize(account.getAmount());
        assertAccountBeforeCreate(account);
        return accountRepository.save(account);
    }

    public Account update(Account account) {
        assertAccountBeforeSave(account);
        assertBigDecimalWithAllocatedSize(account.getAmount());
        assertAccountBeforeUpdate(account);
        return accountRepository.save(account);
    }

    public Page<Account> findAll(int page, int pageSize) {
        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        return accountRepository.findAll(page, pageSize);
    }

    public Transfer makeTransfer(Transfer transfer) {
        assertTransferIllegalFields(transfer);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Account from = accountRepository.findForUpdateById(transfer.getFromAccountId()).orElseThrow(() -> new DataIntegrityException(null, "transfer source id " + transfer.getFromAccountId() + " was not found"));
            Account to = accountRepository.findForUpdateById(transfer.getToAccountId()).orElseThrow(() -> new DataIntegrityException(null, "transfer destination id " + transfer.getToAccountId() + " was not found"));
            //Check if from's account is sufficient for requested amount of transfer
            if (from.getAmount().compareTo(transfer.getAmount()) < 0) {
                throw new RestIllegalArgumentException("Insufficient amount in account " + from.getId());
            }
            BigDecimal exchangeRate = BigDecimal.ONE;
            if (from.getCurrency() != to.getCurrency()) {
                try {
                    exchangeRate = currencyService.getExchangeRateOf(from.getCurrency(), to.getCurrency());
                    assertReceivedExchangeRate(exchangeRate);
                } catch (Exception e) {
                    throw new ExternalServiceException("Currency service call exception converting " + from.getCurrency() + ", to " + to.getCurrency(), null, e);
                }
            }
            BigDecimal toAdd = calculateToAddAmount(to.getAmount(), transfer.getAmount(), exchangeRate);
            from.setAmount(from.getAmount().subtract(transfer.getAmount()));
            to.setAmount(toAdd);
            Transfer dbTransfer = transferRepository.save(prepareTransferForSave(transfer, from, to, exchangeRate));
            transaction.commit();
            return dbTransfer;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public Page<Transfer> findAllTransfersByAccountId(Long accountId, int page, int pageSize) {
        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        return transferRepository.findAllByAccountId(accountId, page, pageSize);
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
            throw new RestIllegalArgumentException("transfer was null");
        }
        if (transfer.getFromAccountId() == null || transfer.getFromAccountId() <= 0) {
            throw new RestIllegalArgumentException("transfer source was illegal");
        }
        if (transfer.getToAccountId() == null || transfer.getToAccountId() <= 0) {
            throw new RestIllegalArgumentException("transfer destination was illegal");
        }
        if (transfer.getToAccountId().equals(transfer.getFromAccountId())) {
            throw new RestIllegalArgumentException("transfer source and destination was the same");
        }
        if (transfer.getAmount() == null || transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RestIllegalArgumentException("transfer amount must be bigger than 0");
        }
        assertBigDecimalWithAllocatedSize(transfer.getAmount());
    }

    private void assertBigDecimalWithAllocatedSize(BigDecimal bigDecimal) {
        if (getDigitsCount(bigDecimal) > BaseModel.MAX_SUPPORTED_MONEY) {
            throw new RestIllegalArgumentException("transfer amount is bigger than allocated size");
        }
        if (getFractionsCount(bigDecimal) > BaseModel.MAX_SUPPORTED_MONEY_FRACTION) {
            throw new RestIllegalArgumentException("transfer amount is bigger than allocated size");
        }
    }

    private BigDecimal calculateToAddAmount(BigDecimal current, BigDecimal transferred, BigDecimal exchangeRate) {
        BigDecimal toAdd = current.add(transferred.multiply(exchangeRate));
        if (getDigitsCount(toAdd) > Account.MAX_SUPPORTED_MONEY + Account.SUPPORTED_MONEY_SAFE_GUARD ||
                getFractionsCount(toAdd) > Account.MAX_SUPPORTED_MONEY_FRACTION) {
            throw new RestUnsupportedOperationException("Calculated money is too big " + toAdd);
        }
        return toAdd;
    }

    private void assertAccountBeforeSave(Account account) {
        if (account == null) {
            throw new RestIllegalArgumentException("Null account");
        }
        if (account.getAmount() == null) {
            throw new RestIllegalArgumentException("Account amount is null");
        }
        if (account.getCurrency() == null) {
            throw new RestIllegalArgumentException("Account currency is null");
        }
        if (account.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RestIllegalArgumentException("Account amount equals or smaller than 0");
        }
    }

    private void assertAccountBeforeCreate(Account account) {
        if (account.getId() != null) {
            throw new RestIllegalArgumentException("Id should be null for creating");
        }
        if (account.getVersion() != null) {
            throw new RestIllegalArgumentException("Version should be null for creating");
        }
    }

    private void assertAccountBeforeUpdate(Account account) {
        if (account.getId() == null || account.getId() <= 0) {
            throw new RestIllegalArgumentException("Cannot update without proper id field (not null and > 0), id =" + account.getId());
        }
        if (account.getVersion() == null || account.getVersion() < 0) {
            throw new RestIllegalArgumentException("Cannot update without proper version field (not null and > 0), version =" + account.getVersion());
        }
    }

    private void assertReceivedExchangeRate(BigDecimal exchangeRate) {
        if (exchangeRate == null) {
            throw new ExternalServiceException("service null return", null);
        }
        if (getDigitsCount(exchangeRate) > Transfer.EXCHANGE_RATE_MAX_DIGITS ||
                getFractionsCount(exchangeRate) > Transfer.EXCHANGE_RATE_MAX_FRAGMENTS) {
            throw new RestUnsupportedOperationException("Returned exchange rate was too big " + exchangeRate);
        }
    }
}
