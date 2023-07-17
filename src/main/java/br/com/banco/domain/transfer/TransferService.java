package br.com.banco.domain.transfer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
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

    public TransferListDTO getTransfers(Pageable pageable, TransferSearchForm transferSearchForm) {
        LocalDate dataInicial = transferSearchForm.getTransferStartDate();
        LocalDate dataFinal = transferSearchForm.getTransferEndDate();
        String nomeOperadorTransacao = transferSearchForm.getTransactionOperatorName();
        Integer contaId = transferSearchForm.getAccount();

        // Cria uma especificação (Specification) para a busca com base nos parâmetros fornecidos
        Specification<Transfer> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("account"), contaId));

            // Filtra por data inicial, se fornecida
            if (dataInicial != null) {
                ZonedDateTime startOfDay = dataInicial.atStartOfDay(ZoneOffset.UTC);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("transferDate"), startOfDay));
            }

            // Filtra por data final, se fornecida
            if (dataFinal != null) {
                ZonedDateTime endOfDay = dataFinal.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("transferDate"), endOfDay));
            }

            // Filtra por nome do operador de transação, se fornecido
            if (nomeOperadorTransacao != null && !nomeOperadorTransacao.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("transactionOperatorName"), nomeOperadorTransacao));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // Realiza a busca com base na especificação, sem a paginação
        List<Transfer> transfers = transferRepository.findAll(spec, pageable.getSort());

        // Calcula a soma dos valores da classe Transfer
        BigDecimal balance = transfers.stream()
                .map(Transfer::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Realiza a paginação dos resultados
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        int start = pageNumber * pageSize;
        int end = Math.min((start + pageable.getPageSize()), transfers.size());
        Page<Transfer> page = new PageImpl<>(transfers.subList(start, end), pageable, transfers.size());

        TransferListDTO transferListDTO = new TransferListDTO(page, balance);

        return transferListDTO;
    }
}
