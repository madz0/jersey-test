package com.github.madz0.revolut.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Currency {
    USD(java.util.Currency.getInstance("USD").getDefaultFractionDigits()),
    EUR(java.util.Currency.getInstance("EUR").getDefaultFractionDigits()),
    ;
    private int fraction;
}
