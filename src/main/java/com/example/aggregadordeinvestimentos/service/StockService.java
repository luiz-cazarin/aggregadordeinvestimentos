package com.example.aggregadordeinvestimentos.service;

import com.example.aggregadordeinvestimentos.controller.dto.CreateStockDto;
import com.example.aggregadordeinvestimentos.entity.Stock;
import com.example.aggregadordeinvestimentos.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }


    public void createStock(CreateStockDto createStockDto) {
        // DTO -> ENTITY
        var stock = new Stock(
                createStockDto.stock_id(),
                createStockDto.description()
        );

        stockRepository.save(stock);
    }
}
