package com.mcorp.transfer.dao.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "accounts")
@NamedQueries({
        @NamedQuery(name = "Account.findAccountByNumberAndSortCode",
                query = "SELECT a FROM Account a WHERE a.accountNumber = :accountNumber and a.sortCode = :sortCode"),
        @NamedQuery(name = "Account.findAll",
                query = "SELECT a FROM Account a")
})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "owner_full_name")
    private String ownerFullName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "sort_code")
    private String sortCode;

    @Column(name = "balance")
    private BigDecimal balance;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @Version
    private Integer version;

    public Account() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(ownerFullName, account.ownerFullName) &&
                Objects.equals(accountNumber, account.accountNumber) &&
                Objects.equals(sortCode, account.sortCode) &&
                Objects.equals(balance, account.balance) &&
                Objects.equals(createDate, account.createDate) &&
                Objects.equals(modifyDate, account.modifyDate) &&
                Objects.equals(version, account.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerFullName, accountNumber, sortCode, balance, createDate, modifyDate, version);
    }
}
