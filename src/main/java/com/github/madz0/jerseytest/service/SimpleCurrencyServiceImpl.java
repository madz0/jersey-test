package com.github.madz0.jerseytest.service;

import com.github.madz0.jerseytest.model.Currency;

import java.math.BigDecimal;

public class SimpleCurrencyServiceImpl implements CurrencyService {
    @Override
    public BigDecimal getExchangeRateOf(Currency from, Currency to) {
        //TODO just a simple demonstration
        return BigDecimal.ONE;
    }
}
