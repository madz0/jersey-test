package com.github.madz0.revolut.service;

import com.github.madz0.revolut.exception.DataIntegrityException;
import com.github.madz0.revolut.model.Account;
import com.github.madz0.revolut.repository.AccountRepository;
import com.github.madz0.revolut.repository.Page;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new DataIntegrityException("Could not find id " + id));
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Page<Account> findAll(int page, int pageSize) {
        return accountRepository.findAll(page, pageSize);
    }
}
