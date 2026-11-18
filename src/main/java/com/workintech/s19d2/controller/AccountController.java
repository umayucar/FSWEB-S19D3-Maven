package com.workintech.s19d2.controller;

import com.workintech.s19d2.entity.Account;
import com.workintech.s19d2.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/account", "/accounts"})
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping({"", "/"})
    public ResponseEntity<List<Account>> findAll() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(accountService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Account> save(
            @RequestBody Account account) {

        return ResponseEntity.ok(accountService.save(account));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> update(
            @PathVariable Long id,
            @RequestBody Account account) {

        return ResponseEntity.ok(
                accountService.update(id, account)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Account> delete(
            @PathVariable Long id) {

        return ResponseEntity.ok(accountService.delete(id));
    }
}