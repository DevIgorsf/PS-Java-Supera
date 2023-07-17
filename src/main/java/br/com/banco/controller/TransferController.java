package br.com.banco.controller;

import br.com.banco.domain.transfer.*;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<TransferListDTO> getAllSearch(
            @PageableDefault(size = 4, sort = {"transferDate"}) Pageable pageable,
            @RequestBody TransferSearchForm transferSearchForm) {
        TransferListDTO page = transferService.getTransfers(pageable, transferSearchForm);

        return ResponseEntity.ok().body(page);
    }
}
