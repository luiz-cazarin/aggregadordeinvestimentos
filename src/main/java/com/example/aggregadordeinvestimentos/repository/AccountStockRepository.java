package com.example.aggregadordeinvestimentos.repository;

import com.example.aggregadordeinvestimentos.entity.AccountStock;
import com.example.aggregadordeinvestimentos.entity.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}
