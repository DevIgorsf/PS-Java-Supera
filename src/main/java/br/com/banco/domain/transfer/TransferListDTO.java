package br.com.banco.domain.transfer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class TransferListDTO {
    private Page<Transfer> page;
    private BigDecimal balance;
}
