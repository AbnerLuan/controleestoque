package com.luan.controleestoque.service;

import com.luan.controleestoque.model.Relatorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RelatorioService {

    private final ProdutoService produtoService;
    private final FiadoService fiadoService;
    private final VendaService vendaService;
    private final GastoService gastoService;

    private final GraficoService graficoService;

    private final CaixaService caixaService;
    @Autowired
    public RelatorioService(ProdutoService produtoService, FiadoService fiadoService, VendaService vendaService,
                            GastoService gastoService, CaixaService caixaService, GraficoService graficoService) {
        this.produtoService = produtoService;
        this.fiadoService = fiadoService;
        this.vendaService = vendaService;
        this.gastoService = gastoService;
        this.caixaService = caixaService;
        this.graficoService = graficoService;
    }

    public Relatorio gerarRelatorio() {
        Relatorio relatorio = new Relatorio();

        //faturamento
        relatorio.setFaturamentoAnual(vendaService.obterValorTotalFaturamentoAnual());
        relatorio.setFaturamentoMedioMensal(calculaMediaMensal(relatorio.getFaturamentoAnual()));
        relatorio.setFaturamentoMesAtual(vendaService.obterValorFaturamentoMesAtual());

        // gastos
        relatorio.setGastosAnual(gastoService.obterValorTotalGastoAnual());
        relatorio.setGastosMedioMensal(calculaMediaMensal(relatorio.getGastosAnual()));
        relatorio.setGastosMesAtual(gastoService.obterValorGastoMesAtual());

        //lucros
        relatorio.setLucroAnual(vendaService.obterValorTotalLucroAnual());
        relatorio.setLucroMedioMensal(calculaMediaMensal(relatorio.getLucroAnual()));
        relatorio.setLucroMesAtual(vendaService.obterValorLucroMesAtual());

        //ativos
        relatorio.setAtivoEstoque(produtoService.obterValorTotalEstoque());
        relatorio.setAtivoCaixa(caixaService.buscarUltimoValorSaldoCaixa());
        relatorio.setAtivoDevedores(fiadoService.obterValorTotalFiado());
        relatorio.setAtivoTotal(relatorio.getAtivoEstoque() + relatorio.getAtivoCaixa() + relatorio.getAtivoDevedores());
    //    graficoService.calcularAtivoTotal(relatorio);

        return relatorio;
    }

    public double calculaMediaMensal(double valorTotal) {
        LocalDate dataAtual = LocalDate.now();
        int numeroMeses = dataAtual.getMonthValue();
        return valorTotal / numeroMeses;
    }

}
