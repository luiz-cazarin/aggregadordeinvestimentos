package com.example.aggregadordeinvestimentos.entity;

import jakarta.persistence.*;
import jdk.jfr.Description;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_accounts")
public class Account {
    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID account_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "account")
    @PrimaryKeyJoinColumn
    private BillingAddress billingAddress;

    @OneToMany(mappedBy = "account")
    private List<AccountStock> accountStocks;

    @Column(name = "description")
    private String description;

    public Account(UUID account_id, User user, BillingAddress billingAddress, String description) {
        this.account_id = account_id;
        this.user = user;
        this.billingAddress = billingAddress;
        this.description = description;
    }

    public Account() {
    }

    public UUID getAccount_id() {
        return account_id;
    }

    public void setAccount_id(UUID account_id) {
        this.account_id = account_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public List<AccountStock> getAccountStocks() {
        return accountStocks;
    }

    public void setAccountStocks(List<AccountStock> accountStocks) {
        this.accountStocks = accountStocks;
    }
}
