package com.github.madz0.jerseytest.service;

import com.github.madz0.jerseytest.model.Currency;

import java.math.BigDecimal;

public interface CurrencyService {
    BigDecimal getExchangeRateOf(Currency from, Currency to);
}
