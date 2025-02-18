package com.example.aggregadordeinvestimentos.service;

import com.example.aggregadordeinvestimentos.client.BrapiClient;
import com.example.aggregadordeinvestimentos.client.dto.BrapiResponseDto;
import com.example.aggregadordeinvestimentos.client.dto.StockDto;
import com.example.aggregadordeinvestimentos.controller.dto.AcccountStockResponseDto;
import com.example.aggregadordeinvestimentos.controller.dto.AssociateAccountStockDto;
import com.example.aggregadordeinvestimentos.controller.dto.CreateStockDto;
import com.example.aggregadordeinvestimentos.entity.*;
import com.example.aggregadordeinvestimentos.repository.AccountRepository;
import com.example.aggregadordeinvestimentos.repository.AccountStockRepository;
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
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    /*
     Arrange - Configuração
     Act - Ação
     Assert - Verificação
    */

    // Criando um mock de AccountRepository
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountStockRepository accountStockRepository;

    // Criando um mock de StockRepository
    @Mock
    private StockRepository stockRepository;

    @Mock
    private BrapiClient brapiClient;

    // Injetando o mock de StockRepository em StockService
    @InjectMocks
    private AccountService accountService;

    // Criando um captor para capturar o usuário que está sendo salvo
    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;

    @Nested
    class associateStock {

        @Value("#{environment.TOKEN}")
        private String TOKEN;


        @Test
        @DisplayName("Should associate stock with success")
        void showAssociateStockWithSuccess() {
            // Arrange
            var accountId = UUID.randomUUID();
            var stockId = "PETR4";

            var userId = UUID.randomUUID();

            var user = new User(
                    userId,
                    "username",
                    "user@mail.com",
                    "1234",
                    Instant.now(),
                    null
            );

            var account = new Account(
                    accountId,
                    user,
                    null,
                    new ArrayList<>(),
                    "description"
            );

            var stock = new Stock(
                    stockId,
                    "Petrobras"
            );

            var dto = new AssociateAccountStockDto(
                    stockId,
                    10
            );

            var expectedEntity = new AccountStock(
                    new AccountStockId(account.getAccount_id(), stock.getStock_id()),
                    account,
                    stock,
                    dto.quantity()
            );


            doReturn(Optional.of(account)).when(accountRepository).findById(accountId);
            doReturn(Optional.of(stock)).when(stockRepository).findById(stockId);
            doReturn(expectedEntity).when(accountStockRepository).save(any());
            // Act
            accountService.associateStock(accountId.toString(), dto);

            // Assert
            verify(accountRepository).findById(uuidCaptor.capture());
            verify(stockRepository).findById(stockId);
            verify(accountStockRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("Should get stock with success")
        void showGetStockWithSuccess() {
            // Arrange
            var accountId = UUID.randomUUID();
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "user@mail.com",
                    "1234",
                    Instant.now(),
                    null
            );
            var account = new Account(
                    accountId,
                    user,
                    null,
                    new ArrayList<>(),
                    "description"
            );
            var stock = new Stock("PETR4", "Petrobras");

            var accountStock = new AccountStock(
                    new AccountStockId(account.getAccount_id(), stock.getStock_id()),
                    account,
                    stock,
                    10
            );

            var stockDto = new StockDto(3.0);

            // Adiciona a associação corretamente
            account.getAccountStocks().add(accountStock);

            // simulando retorno do repository
            var mockResponse = new BrapiResponseDto(List.of(stockDto));

            doReturn(Optional.of(account)).when(accountRepository).findById(accountId);
            doReturn(mockResponse).when(brapiClient).getQuote(TOKEN, "PETR4");

            // Act
            var output = accountService.getStocks(accountId.toString());

            // Assert
            verify(accountRepository, times(1)).findById(accountId);
            assertNotNull(output);
            assertEquals(1, output.size());
            assertEquals(10, output.get(0).quantity());
        }
    }
}


















