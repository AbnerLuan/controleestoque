package com.luan.controleestoque.model;

import com.luan.controleestoque.model.Enum.TipoTransacao;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Caixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caixaId;

    private TipoTransacao tipoTransacao;

    private double valorTransacao;

    private double saldoCaixa;

    private LocalDate dataLancamento = LocalDate.now();

    private String observacao;

}
