package com.github.madz0.revolut.model;

import lombok.Getter;

import static com.github.madz0.revolut.model.BaseModel.MAX_SUPPORTED_MONEY_FRACTION;

@Getter
public enum Currency {
    USD(java.util.Currency.getInstance("USD").getDefaultFractionDigits()),
    EUR(java.util.Currency.getInstance("EUR").getDefaultFractionDigits()),
    ;
    private int fraction;

    Currency(int fraction) throws ExceptionInInitializerError {
        if (fraction > MAX_SUPPORTED_MONEY_FRACTION) {
            throw new ExceptionInInitializerError("Fraction is not supported for the storage");
        }
        this.fraction = fraction;
    }
}
