package br.com.banco.controller;

import br.com.banco.domain.transfer.Transfer;
import br.com.banco.domain.transfer.TransferSearchForm;
import br.com.banco.domain.transfer.TransferService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/transferencia")
public class TransferController {

    @Autowired
    private TransferService transferService;
    @PostMapping("/search")
    public ResponseEntity<Page<Transfer>> getAllSearch(
            @PageableDefault(size = 4, sort = {"transferDate"}) Pageable pageable,
            @RequestBody TransferSearchForm transferSearchForm) {
        Page<Transfer> page = transferService.getTransfers(pageable, transferSearchForm);

        // Retorne as transferências encontradas
        return ResponseEntity.ok().body(page);
    }
}
