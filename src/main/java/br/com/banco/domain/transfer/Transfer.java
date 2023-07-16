package br.com.banco.domain.transfer;

import br.com.banco.domain.account.Account;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "transferencia")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "data_transferencia")
    private ZonedDateTime transferDate;
    @Column(name = "valor")
    private BigDecimal value;
    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TypeTransfer typeTransfer;
    @Column(name = "nome_operador_transacao")
    private String transactionOperatorName;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id")
    private Account account;
}
