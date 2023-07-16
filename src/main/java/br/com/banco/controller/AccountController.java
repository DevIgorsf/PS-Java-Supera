package br.com.banco.controller;

import br.com.banco.domain.account.Account;
import br.com.banco.domain.account.AccountRepository;
import br.com.banco.domain.transfer.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/conta")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Integer id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if(accountOptional.isEmpty()) {
            throw new EntityNotFoundException("Conta não encontrada");
        }
        var account = accountOptional.get();

        return ResponseEntity.ok().body(account);
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Integer id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if(accountOptional.isEmpty()) {
            throw new EntityNotFoundException("Conta não encontrada");
        }
        var balance = accountOptional.get().getListTransfer().stream()
                .map(Transfer::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);;

        return ResponseEntity.ok().body(balance);
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accountList = accountRepository.findAll();
        return ResponseEntity.ok().body(accountList);
    }

}
