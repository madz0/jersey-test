package com.github.madz0.revolut.repository;

import com.github.madz0.revolut.model.Transfer;

import java.util.Optional;

public interface TransferRepository {
    Transfer save(Transfer transfer);
    Optional<Transfer> findTransferByAccountIdAndId(Long accountId, Long transferId);
    Page<Transfer> findAllByAccountId(Long accountId, int page, int size);
}
