package com.github.madz0.jerseytest.repository;

import com.github.madz0.jerseytest.model.Account;

import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findById(Long id);
    Page<Account> findAll(int page, int size);
    Optional<Account> findForUpdateById(Long id);
}
