package com.luan.controleestoque.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long compraId;

    private String nomeFornecedor;

    private String canalCompra;

    private Long valorTotalCompra;

    @OneToMany(fetch= FetchType.LAZY, cascade= CascadeType.ALL, mappedBy = "compra")
    private List<ItemCompra> itensCompra;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataCompra = LocalDate.now();


}
