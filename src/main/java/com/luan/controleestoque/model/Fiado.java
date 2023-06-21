package com.luan.controleestoque.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fiado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fiadoId;

    private String nomeCliente;

    private String celularCliente;

    private Double valorTotal;

    private Double valorPendente;

    private LocalDate dataRegistro = LocalDate.now();

    private String observacoes;

    @OneToMany(fetch= FetchType.LAZY, cascade= CascadeType.ALL, mappedBy = "fiado")
    private List<Pagamento> pagamento;

}
