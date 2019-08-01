package com.github.madz0.revolut.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyUtils {
    public static BigDecimal getRounded(int fraction, BigDecimal amount) {
        return amount.setScale(fraction, RoundingMode.HALF_EVEN);
    }
}
