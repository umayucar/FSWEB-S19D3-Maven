package com.workintech.s19d2.service;

import com.workintech.s19d2.dao.AccountRepository;
import com.workintech.s19d2.entity.Account;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account not found: " + id
                ));
    }

    @Override
    @Transactional
    public Account save(Account account) {
        account.setId(null);

        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account update(Long id, Account account) {

        Account existingAccount = findById(id);
        existingAccount.setName(account.getName());

        return accountRepository.save(existingAccount);
    }

    @Override
    @Transactional
    public Account delete(Long id) {

        Account account = findById(id);
        accountRepository.delete(account);

        return account;
    }
}
