package com.github.madz0.jerseytest.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyUtils {
    public static BigDecimal getRounded(int fraction, BigDecimal amount) {
        return amount.setScale(fraction, RoundingMode.HALF_EVEN);
    }

    public static int getDigitsCount(BigDecimal n) {
        return n.signum() == 0 ? 1 : n.precision() - n.scale();
    }

    public static int getFractionsCount(BigDecimal n) {
        return n.stripTrailingZeros().scale();
    }

}
