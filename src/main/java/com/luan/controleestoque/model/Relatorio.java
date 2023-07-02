package com.luan.controleestoque.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Relatorio implements Serializable {

    private double faturamentoAnual;

    private double faturamentoMedioMensal;

    private double faturamentoMesAtual;

    private double gastosAnual;

    private double gastosMedioMensal;

    private double gastosMesAtual;

    private double lucroAnual;

    private double lucroMedioMensal;

    private double lucroMesAtual;

    private double ativoEstoque;

    private double ativoCaixa;

    private double ativoDevedores;

    private double ativoTotal;

}
