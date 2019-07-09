package com.mcorp.transfer.model;

import java.util.Objects;
import java.util.StringJoiner;

public class AccountDto {

    private String sortCode;

    private String number;

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDto that = (AccountDto) o;
        return Objects.equals(sortCode, that.sortCode) &&
                Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sortCode, number);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AccountDto.class.getSimpleName() + "[", "]")
                .add("sortCode='" + sortCode + "'")
                .add("number='" + number + "'")
                .toString();
    }


    public static final class AccountDtoBuilder {
        private String sortCode;
        private String number;

        private AccountDtoBuilder() {
        }

        public static AccountDtoBuilder anAccountDto() {
            return new AccountDtoBuilder();
        }

        public AccountDtoBuilder withSortCode(String sortCode) {
            this.sortCode = sortCode;
            return this;
        }

        public AccountDtoBuilder withNumber(String number) {
            this.number = number;
            return this;
        }

        public AccountDto build() {
            AccountDto accountDto = new AccountDto();
            accountDto.setSortCode(sortCode);
            accountDto.setNumber(number);
            return accountDto;
        }
    }
}
