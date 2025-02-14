package com.example.aggregadordeinvestimentos.service;

import com.example.aggregadordeinvestimentos.controller.StockController;
import com.example.aggregadordeinvestimentos.controller.dto.CreateStockDto;
import com.example.aggregadordeinvestimentos.entity.Stock;
import com.example.aggregadordeinvestimentos.repository.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {
    /*
     Arrange - Configuração
     Act - Ação
     Assert - Verificação
    */

    // Criando um mock de StockRepository
    @Mock
    private StockRepository stockRepository;

    // Injetando o mock de StockRepository em StockService
    @InjectMocks
    private StockService stockService;

    @Captor
    private ArgumentCaptor<Stock> stockArgumentCaptor;

    @Nested
    class createStock {

        @Test
        @DisplayName("Should create stock with success")
        void shouldCreateStockWithSuccess() {
            // Arrange
            var stock = new Stock(
                    "PETR4",
                    "Petrobras"
            );

            var stockDto = new CreateStockDto(
                    stock.getStock_id(),
                    stock.getDescription()
            );

            // o stock argument captor captura o argumento que foi passado
            // para o método save (na classe StockRepository)
            doReturn(stock).when(stockRepository).save(stockArgumentCaptor.capture());
            // Act
            stockService.createStock(stockDto);

            // Assert
            verify(stockRepository, times(1)).save(stockArgumentCaptor.capture());
        }

        @Test
        @DisplayName("Should not create stock with success")
        void shouldNotCreateStockWithSuccess() {
            // Arrange
            var input = new CreateStockDto(
                    "Petro4",
                    "petrobras"
            );

            // o stock argument captor captura o argumento que foi passado
            // para o método save (na classe StockRepository)
            doThrow(new RuntimeException()).when(stockRepository).save(any());

            // Act & Assert
            assertThrows(RuntimeException.class, () -> stockService.createStock(input));
            verify(stockRepository, times(1)).save(any());

        }
    }
}
