package br.com.banco.domain.transfer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransferService {
    @Autowired
    private TransferRepository transferRepository;

    public Page<Transfer> getTransfers(Pageable pageable, TransferSearchForm transferSearchForm) {
        LocalDate dataInicial = transferSearchForm.getTransferStartDate();
        LocalDate dataFinal = transferSearchForm.getTransferEndDate();
        String nomeOperadorTransacao = transferSearchForm.getTransactionOperatorName();
        Integer contaId = transferSearchForm.getAccount();

        // Crie uma especificação (Specification) para a busca com base nos parâmetros fornecidos
        Specification<Transfer> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("account"), contaId));

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

        // Realize a busca com base na especificação, página e ordenação
        Page<Transfer> page = transferRepository.findAll(spec, pageable);
        return page;
    }
}
