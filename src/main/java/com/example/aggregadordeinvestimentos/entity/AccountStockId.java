package com.example.aggregadordeinvestimentos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class AccountStockId {

    @Column(name = "account_id")
    private UUID account_id;

    @Column(name = "stock_id")
    private String stock_id;

    public AccountStockId() {
    }

    public AccountStockId(UUID account_id, String stock_id) {
        this.account_id = account_id;
        this.stock_id = stock_id;
    }

    public UUID getAccount_id() {
        return account_id;
    }

    public void setAccount_id(UUID account_id) {
        this.account_id = account_id;
    }

    public String getStock_id() {
        return stock_id;
    }

    public void setStock_id(String stock_id) {
        this.stock_id = stock_id;
    }
}
