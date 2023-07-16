package br.com.banco.domain.account;

import br.com.banco.domain.transfer.Transfer;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "conta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conta")
    private Integer id;
    @Column(name = "nome_responsavel")
    private String responsibleName;
    @JsonManagedReference
    @OneToMany(mappedBy = "account")
    private Set<Transfer> listTransfer;
}
