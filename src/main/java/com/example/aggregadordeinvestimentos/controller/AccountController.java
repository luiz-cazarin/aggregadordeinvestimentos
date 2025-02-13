package com.example.aggregadordeinvestimentos.controller;

import com.example.aggregadordeinvestimentos.controller.dto.AssociateAccountStockDto;
import com.example.aggregadordeinvestimentos.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{account_id}/stocks")
    public ResponseEntity<Void> associateStock(
            @PathVariable("account_id") String accountId,
            @RequestBody AssociateAccountStockDto dto) {
        accountService.associateStock(accountId, dto);
        return ResponseEntity.ok().build();
    }

}
