package com.example.aggregadordeinvestimentos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_stocks")
public class Stock {

    @Id
    @Column(name = "stock_id") // PETR4 etc
    private String stock_id;

    @Column(name = "description")
    private String description;

    public Stock() {
    }

    public Stock(String stock_id, String description) {
        this.stock_id = stock_id;
        this.description = description;
    }

    public String getStock_id() {
        return stock_id;
    }

    public void setStock_id(String stock_id) {
        this.stock_id = stock_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
