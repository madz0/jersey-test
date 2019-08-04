package com.github.madz0.revolut.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.github.madz0.revolut.util.CurrencyUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transfers", indexes = {@Index(name = "from_account_id", columnList = "from_account_id"), @Index(name = "to_account_id", columnList = "to_account_id")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transfer extends BaseModel {
    public final static int EXCHANGE_RATE_MAX_DIGITS = 13;
    public final static int EXCHANGE_RATE_MAX_FRAGMENTS = 4;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id")
    private Account from;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id")
    private Account to;

    @Digits(integer = MAX_SUPPORTED_MONEY, fraction = MAX_SUPPORTED_MONEY_FRACTION, message = "Wrong value for money")
    @Column(columnDefinition = "DECIMAL(" + (MAX_SUPPORTED_MONEY + SUPPORTED_MONEY_SAFE_GUARD) + ", " + MAX_SUPPORTED_MONEY_FRACTION + ")", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency fromCurrency;

    @Enumerated(EnumType.STRING)
    private Currency toCurrency;

    @Column(columnDefinition = "DECIMAL(" + EXCHANGE_RATE_MAX_DIGITS + ", " + EXCHANGE_RATE_MAX_FRAGMENTS + ")", nullable = false)
    private BigDecimal exchangeRate;

    @JsonGetter("destinationAmount")
    public BigDecimal getAmountInDestinationCurrency() {
        if (toCurrency == null || amount == null || exchangeRate == null) {
            return null;
        }
        return CurrencyUtils.getRounded(toCurrency.getFraction(), amount.multiply(exchangeRate));
    }

    @JsonGetter
    public Long getFromAccountId() {
        if (from == null) {
            return null;
        }
        return from.getId();
    }

    @JsonGetter
    public Long getToAccountId() {
        if (to == null) {
            return null;
        }
        return to.getId();
    }

    @JsonSetter
    public void setFrom(Account account) {
        this.from = account;
    }

    @JsonSetter
    public void setTo(Account account) {
        this.to = account;
    }

    @JsonGetter
    public Currency getOriginCurrency() {
        return fromCurrency;
    }

    @JsonGetter
    public Currency getDestinationCurrency() {
        return toCurrency;
    }

    @JsonGetter("amount")
    public BigDecimal getRoundedAmount() {
        if (fromCurrency == null || amount == null) {
            return null;
        }
        return CurrencyUtils.getRounded(fromCurrency.getFraction(), amount);
    }

    @JsonGetter
    public LocalDateTime getExchangeDateAndTime() {
        if (getCreatedDate() == null) {
            return null;
        }
        return LocalDateTime.ofInstant(getCreatedDate().toInstant(), ZoneId.systemDefault());
    }
}
