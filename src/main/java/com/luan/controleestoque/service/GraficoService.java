package com.luan.controleestoque.service;

import com.luan.controleestoque.model.Grafico;
import com.luan.controleestoque.model.Relatorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GraficoService {

    private final ProdutoService produtoService;
    private final FiadoService fiadoService;
    private final VendaService vendaService;
    private final GastoService gastoService;
    @Autowired
    public GraficoService(ProdutoService produtoService, FiadoService fiadoService, VendaService vendaService,
                            GastoService gastoService) {
        this.produtoService = produtoService;
        this.fiadoService = fiadoService;
        this.vendaService = vendaService;
        this.gastoService = gastoService;
    }
    public List<Grafico> getDadosGrafico() {
        List<Grafico> dadosGrafico = new ArrayList<>();

        Grafico graficoFaturamento = criarGraficoFaturamento();
        dadosGrafico.add(graficoFaturamento);

        Grafico graficoGasto = criarGraficoGasto();
        dadosGrafico.add(graficoGasto);

        Grafico graficoLucro = criarGraficoLucro();
        dadosGrafico.add(graficoLucro);

        Grafico graficoAtivo = criarGraficoAtivo();
        dadosGrafico.add(graficoAtivo);

        return dadosGrafico;
    }

    private Grafico criarGraficoFaturamento() {
        Grafico graficoFaturamento = new Grafico();
        graficoFaturamento.setLabel("Faturamento");
        graficoFaturamento.setData(vendaService.getValoresFaturamentoMensalFromDatabase());

        return graficoFaturamento;
    }

    private Grafico criarGraficoGasto() {
        Grafico graficoGasto = new Grafico();
        graficoGasto.setLabel("Gastos");
        graficoGasto.setData(gastoService.getValoresGastoMensalFromDatabase());

        return graficoGasto;
    }

    private Grafico criarGraficoLucro() {
        Grafico graficoLucro = new Grafico();
        graficoLucro.setLabel("Lucros");
        graficoLucro.setData(vendaService.getValoresLucroMensalFromDatabase());
        return graficoLucro;
    }

    private Grafico criarGraficoAtivo() {
        Grafico graficoAtivo = new Grafico();
        graficoAtivo.setLabel("Ativos");
        graficoAtivo.setData(vendaService.getValoresFaturamentoMensalFromDatabase());
        return graficoAtivo;
    }

//    public double calcularAtivoTotal(Relatorio relatorio){
//        double valorTotalAtivo;
//        double valorTotalEstoque = relatorio.getAtivoEstoque();
//        double valorTotalCaixa = relatorio.getAtivoCaixa();
//        double valorTotalDevedores = relatorio.getAtivoDevedores();
//        valorTotalAtivo = valorTotalEstoque + valorTotalCaixa + valorTotalDevedores;
//        return valorTotalAtivo;
//    }


}
