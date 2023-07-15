package br.com.banco.controller;

import br.com.banco.domain.transfer.Transfer;
import br.com.banco.domain.transfer.TransferRepository;
import br.com.banco.domain.transfer.TransferSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/transferencia")
public class TransferController {

    @Autowired
    private TransferRepository transferRepository;

    @GetMapping
    public ResponseEntity<Page<Transfer>> getAll(@PageableDefault(size = 4, sort = {"transferDate"}) Pageable paginacao) {
        Page<Transfer> transferList = transferRepository.findAll(paginacao);
        return ResponseEntity.ok().body(transferList);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Transfer>> getAllSearch(
            @PageableDefault(size = 4, sort = {"transferDate"}) Pageable paginacao,
            @RequestBody TransferSearchForm transferSearchForm) {
        LocalDate dataInicial = transferSearchForm.getTransferStartDate();
        LocalDate dataFinal = transferSearchForm.getTransferEndDate();
        String nomeOperadorTransacao = transferSearchForm.getTransactionOperatorName();

        // Crie uma especificação (Specification) para a busca com base nos parâmetros fornecidos
        Specification<Transfer> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtre por data inicial, se fornecida
            if (dataInicial != null) {
                ZonedDateTime startOfDay = dataInicial.atStartOfDay(ZoneOffset.UTC);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("transferDate"), startOfDay));
            }

            // Filtre por data final, se fornecida
            if (dataFinal != null) {
                ZonedDateTime endOfDay = dataFinal.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("transferDate"), endOfDay));
            }

            // Filtre por nome do operador de transação, se fornecido
            if (nomeOperadorTransacao != null && !nomeOperadorTransacao.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("transactionOperatorName"), nomeOperadorTransacao));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // Defina a página e a ordenação (opcional) para a busca
        Pageable pageable = PageRequest.of(0, 4, Sort.by("transferDate").descending());

        // Realize a busca com base na especificação, página e ordenação
        Page<Transfer> page = transferRepository.findAll(spec, pageable);

        // Retorne as transferências encontradas
        return ResponseEntity.ok().body(page);
    }
}
