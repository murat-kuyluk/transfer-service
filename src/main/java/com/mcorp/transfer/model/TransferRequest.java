package com.mcorp.transfer.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class TransferRequest {

    private AccountDto from;

    private AccountDto to;

    private BigDecimal amount;

    private String note;

    public AccountDto getFrom() {
        return from;
    }

    public void setFrom(AccountDto from) {
        this.from = from;
    }

    public AccountDto getTo() {
        return to;
    }

    public void setTo(AccountDto to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferRequest that = (TransferRequest) o;
        return Objects.equals(from, that.from) &&
                Objects.equals(to, that.to) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, amount, note);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransferRequest.class.getSimpleName() + "[", "]")
                .add("from=" + from)
                .add("to=" + to)
                .add("amount=" + amount)
                .add("note='" + note + "'")
                .toString();
    }

    public static final class TransferRequestBuilder {
        private AccountDto from;
        private AccountDto to;
        private BigDecimal amount;
        private String note;

        private TransferRequestBuilder() {
        }

        public static TransferRequestBuilder aTransferRequest() {
            return new TransferRequestBuilder();
        }

        public TransferRequestBuilder withFrom(AccountDto from) {
            this.from = from;
            return this;
        }

        public TransferRequestBuilder withTo(AccountDto to) {
            this.to = to;
            return this;
        }

        public TransferRequestBuilder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public TransferRequestBuilder withNote(String note) {
            this.note = note;
            return this;
        }

        public TransferRequest build() {
            TransferRequest transferRequest = new TransferRequest();
            transferRequest.setFrom(from);
            transferRequest.setTo(to);
            transferRequest.setAmount(amount);
            transferRequest.setNote(note);
            return transferRequest;
        }
    }
}
