package com.example.aggregadordeinvestimentos.service;

import com.example.aggregadordeinvestimentos.controller.dto.AssociateAccountStockDto;
import com.example.aggregadordeinvestimentos.entity.AccountStock;
import com.example.aggregadordeinvestimentos.entity.AccountStockId;
import com.example.aggregadordeinvestimentos.repository.AccountRepository;
import com.example.aggregadordeinvestimentos.repository.AccountStockRepository;
import com.example.aggregadordeinvestimentos.repository.StockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private StockRepository stockRepository;
    private AccountStockRepository accountStockRepository;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
    }

    public void associateStock(String accountId, AssociateAccountStockDto dto) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var stock = stockRepository.findById(dto.stock_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // DTO -> ENTITY
        var id = new AccountStockId(account.getAccount_id(), stock.getStock_id());
        var entity = new AccountStock(
                id,
                account,
                stock,
                dto.quantity()
        );

        accountStockRepository.save(entity);
    }
}
