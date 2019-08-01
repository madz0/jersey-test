package com.github.madz0.revolut.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.github.madz0.revolut.util.CurrencyUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account extends BaseModel {
    @Enumerated(EnumType.STRING)
    @NotNull(groups = {Create.class, UpdateCurrency.class}, message = "Wrong value for currency")
    private Currency currency;

    @NotNull(groups = {Create.class, UpdateAmount.class}, message = "Wrong value for money")
    @Digits(integer = 19, fraction = 4, message = "Wrong value for money")
    @Column(columnDefinition = "DECIMAL(19, 4)", nullable = false)
    private BigDecimal amount;

    @JsonGetter("amount")
    public BigDecimal roundAmount() {
        return CurrencyUtils.getRounded(currency.getFraction(), amount);
    }

    public interface Create {
    }

    public interface UpdateCurrency extends Update {
    }

    public interface UpdateAmount extends Update {
    }
}
