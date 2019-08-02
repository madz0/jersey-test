package com.github.madz0.revolut.repository;

import com.github.madz0.revolut.model.Transfer;

public interface TransferRepository {
    Transfer save(Transfer transfer);
    Page<Transfer> findAllByAccountId(Long accountId, int page, int size);
}
