package br.com.banco.domain.transfer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
public class TransferSearchForm {
    @JsonFormat(timezone = "yyyy-MM-dd")
    private LocalDate transferStartDate;
    @JsonFormat(timezone = "yyyy-MM-dd")
    private LocalDate transferEndDate;
    private String transactionOperatorName;
    private Integer account;
}
