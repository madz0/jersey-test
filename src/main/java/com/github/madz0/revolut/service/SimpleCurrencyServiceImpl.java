package com.github.madz0.revolut.service;

import com.github.madz0.revolut.model.Currency;

import java.math.BigDecimal;

public class SimpleCurrencyServiceImpl implements CurrencyService {
    @Override
    public BigDecimal getExchangeRateOf(Currency from, Currency to) {
        //TODO just a simple demonstration
        return BigDecimal.ONE;
    }
}
