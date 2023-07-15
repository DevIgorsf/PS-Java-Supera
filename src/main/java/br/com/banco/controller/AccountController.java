package br.com.banco.controller;

import br.com.banco.domain.account.Account;
import br.com.banco.domain.account.AccountRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/conta")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAllTransfer(@PathVariable Integer id) {
        Account account = accountRepository.findById(id).get();
        return ResponseEntity.ok().body(account);
    }

}
