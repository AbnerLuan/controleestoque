package com.luan.controleestoque.service;

import com.luan.controleestoque.model.Caixa;
import com.luan.controleestoque.model.Enum.TipoTransacao;
import com.luan.controleestoque.model.Gasto;
import com.luan.controleestoque.model.MesValor;
import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.repository.GastoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class GastoService {

    private static final Logger logger = Logger.getLogger(GastoService.class.getName());
    private final GastoRepository gastoRepository;

    private final CaixaService caixaService;

    @Autowired
    public GastoService(GastoRepository gastoRepository, CaixaService caixaService){
        this.gastoRepository = gastoRepository;
        this.caixaService = caixaService;
    }

    public Page<Gasto> findAllPageable(Pageable pageable) {
        logger.log(Level.INFO, "Lista de Gastos Carregada.");
        return gastoRepository.findAll(pageable);
    }


    public Gasto save(Gasto gasto) {
        logger.log(Level.INFO, "Novo Gasto Salvo: " + gasto.getDescricao());
        Gasto gastoSalvo = gastoRepository.save(gasto);
        criarLancamentoCaixa(gastoSalvo);
        return gastoRepository.save(gasto);
    }

    private void criarLancamentoCaixa(Gasto gasto){
        Caixa lancamentoGastoCaixa = new Caixa();
        lancamentoGastoCaixa.setValorTransacao(gasto.getValor());
        lancamentoGastoCaixa.setTipoTransacao(TipoTransacao.SAIDA);
        lancamentoGastoCaixa.setObservacao("Lancamento gasto ID: " + gasto.getGastoId().toString());
        caixaService.save(lancamentoGastoCaixa);
    }

    public void deleteById(Long id) {
        logger.log(Level.INFO, "Gasto excluido com sucesso! id: " + id);
        gastoRepository.deleteById(id);
    }

    public double obterValorTotalGastoAnual() {
        return gastoRepository.calculaValorTotalGastoAnual();
    }

    public double obterValorGastoMesAtual() {
        return gastoRepository.calcularGastoMesAtual();
    }


    public List<Double> getValoresGastoMensalFromDatabase() {
        List<MesValor> mesValores = gastoRepository.getValoresGastoMensalFromDatabase();
        List<Double> valoresGastoMensal = new ArrayList<>(Collections.nCopies(12, 0.0));

        for (MesValor mesValor : mesValores) {
            int monthIndex = mesValor.getMes() - 1; // Ajuste do índice para o mês atual menos 1
            valoresGastoMensal.set(monthIndex, mesValor.getValor());
        }
        return valoresGastoMensal;
    }

}
