package com.example.aggregadordeinvestimentos.service;

import com.example.aggregadordeinvestimentos.client.BrapiClient;
import com.example.aggregadordeinvestimentos.controller.dto.AssociateAccountStockDto;
import com.example.aggregadordeinvestimentos.controller.dto.AcccountStockResponseDto;
import com.example.aggregadordeinvestimentos.entity.AccountStock;
import com.example.aggregadordeinvestimentos.entity.AccountStockId;
import com.example.aggregadordeinvestimentos.repository.AccountRepository;
import com.example.aggregadordeinvestimentos.repository.AccountStockRepository;
import com.example.aggregadordeinvestimentos.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    @Value("#{environment.TOKEN}")
    private String TOKEN;
    private AccountRepository accountRepository;
    private StockRepository stockRepository;
    private AccountStockRepository accountStockRepository;
    private BrapiClient brapiClient;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository, BrapiClient brapiClient) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
        this.brapiClient = brapiClient;
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

    public List<AcccountStockResponseDto> getStocks(String accountId) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // ENTITY -> DTO
        return account.getAccountStocks().stream()
                .map(as -> new AcccountStockResponseDto(
                        as.getStock().getStock_id(),
                        as.getQuantity(),
                        getTotal(as.getQuantity(),
                                as.getStock().getStock_id())
                )).toList();
    }

    private double getTotal(int quantity, String stockId) {
        var response = brapiClient.getQuote(TOKEN, stockId);
        var price = response.results().getFirst().regularMarketPrice();
        return quantity * price;
    }
}
