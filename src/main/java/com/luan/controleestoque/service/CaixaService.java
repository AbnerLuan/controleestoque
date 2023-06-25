package com.luan.controleestoque.service;

import com.luan.controleestoque.model.Caixa;
import com.luan.controleestoque.repository.CaixaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.luan.controleestoque.model.Enum.TipoTransacao;


@Service
public class CaixaService {

    private final CaixaRepository caixaRepository;

    @Autowired
    public CaixaService(CaixaRepository caixaRepository) {
        this.caixaRepository = caixaRepository;
    }

    public Page<Caixa> findAllPageable(Pageable pageable) {
        return caixaRepository.findAll(pageable);
    }

    public Caixa save(Caixa caixa) {
        caixa.setSaldoCaixa(calcularSaldoCaixa(caixa));
        return caixaRepository.save(caixa);
    }

    public double calcularSaldoCaixa(Caixa caixa) {
        double valorTransacao = caixa.getValorTransacao();
        if (caixa.getTipoTransacao() == TipoTransacao.SAIDA) {
            valorTransacao *= -1;
        }
        double saldoAtual = buscarUltimoValorSaldoCaixa();
        double novoSaldoCaixa = saldoAtual + valorTransacao;
        return novoSaldoCaixa;
    }

    private double buscarUltimoValorSaldoCaixa() {
        double ultimoValorSaldoCaixa = caixaRepository.findUltimoValorSaldo();
        return ultimoValorSaldoCaixa;
    }

}
