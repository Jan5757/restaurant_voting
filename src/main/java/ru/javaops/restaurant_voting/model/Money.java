package ru.javaops.restaurant_voting.model;

import java.util.Currency;

public class Money {
    private static final int[] cents = new int[]{1, 10, 100, 1000};
    private long amount;
    private Currency currency;

    public double getAmount() {
        return (double) amount / centFactor();
    }

    private int centFactor() {
        return cents[currency.getDefaultFractionDigits()];
    }

    public Money(long amount, Currency currency) {
        this.currency = currency;
        this.amount = amount * centFactor();
    }

    public Money(double amount, Currency currency) {
        this.currency = currency;
        this.amount = Math.round(amount * centFactor());
    }

    private Money() {
    }

    @Override
    public String toString() {
        return String.valueOf(getAmount());
    }
}
