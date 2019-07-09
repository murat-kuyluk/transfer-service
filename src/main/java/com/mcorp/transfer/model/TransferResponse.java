package com.mcorp.transfer.model;

import java.util.Objects;
import java.util.StringJoiner;

public class TransferResponse {

    private Long transferId;

    private String message;

    public TransferResponse(Long transferId, String message) {
        this.transferId = transferId;
        this.message = message;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransferResponse.class.getSimpleName() + "[", "]")
                .add("transferId=" + transferId)
                .add("message='" + message + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferResponse that = (TransferResponse) o;
        return Objects.equals(transferId, that.transferId) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId, message);
    }
}
