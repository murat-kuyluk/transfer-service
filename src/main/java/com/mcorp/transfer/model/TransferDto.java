package com.mcorp.transfer.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class TransferDto {

    private Long id;
    private String fromAccount;
    private String toAccount;
    private String reference;
    private BigDecimal amount;

    public TransferDto(Long id, String fromAccount, String toAccount, BigDecimal amount, String reference) {
        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.reference = reference;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferDto that = (TransferDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(fromAccount, that.fromAccount) &&
                Objects.equals(toAccount, that.toAccount) &&
                Objects.equals(reference, that.reference) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromAccount, toAccount, reference, amount);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransferDto.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("fromAccount='" + fromAccount + "'")
                .add("toAccount='" + toAccount + "'")
                .add("reference='" + reference + "'")
                .add("amount=" + amount)
                .toString();
    }
}
