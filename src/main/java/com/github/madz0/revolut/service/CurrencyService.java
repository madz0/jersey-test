package com.github.madz0.revolut.service;

import com.github.madz0.revolut.model.Currency;

import java.math.BigDecimal;

public interface CurrencyService {
    BigDecimal getExchangeRateOf(Currency from, Currency to);
}
