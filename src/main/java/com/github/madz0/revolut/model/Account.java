package com.github.madz0.revolut.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.github.madz0.revolut.util.CurrencyUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account extends BaseModel {
    @Enumerated(EnumType.STRING)
    @NotNull(groups = Create.class, message = "Wrong value for currency")
    private Currency currency;

    @NotNull(groups = {Create.class, Update.class}, message = "Wrong value for money")
    @Digits(integer = MAX_SUPPORTED_MONEY, fraction = MAX_SUPPORTED_MONEY_FRACTION, message = "Wrong value for money")
    @Column(columnDefinition = "DECIMAL(" + (MAX_SUPPORTED_MONEY + SUPPORTED_MONEY_SAFE_GUARD) + ", " + MAX_SUPPORTED_MONEY_FRACTION + ")", nullable = false)
    private BigDecimal amount;

    @JsonGetter("amount")
    public BigDecimal roundAmount() {
        if (currency == null) {
            return null;
        }
        return CurrencyUtils.getRounded(currency.getFraction(), amount);
    }

    public interface Create {
    }
}
