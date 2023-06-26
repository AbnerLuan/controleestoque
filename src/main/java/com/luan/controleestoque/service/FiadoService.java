package com.luan.controleestoque.service;

import com.luan.controleestoque.model.Caixa;
import com.luan.controleestoque.model.Enum.TipoTransacao;
import com.luan.controleestoque.model.Fiado;
import com.luan.controleestoque.model.Pagamento;
import com.luan.controleestoque.repository.FiadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class FiadoService {


    private static final Logger logger = Logger.getLogger(GastoService.class.getName());
    private final FiadoRepository fiadoRepository;
    private final CaixaService caixaService;
    @Autowired
    public FiadoService(FiadoRepository fiadoRepository, CaixaService caixaService){
        this.fiadoRepository = fiadoRepository;
        this.caixaService = caixaService;
    }

    public List<Fiado> findAll() {
        return fiadoRepository.findAll();
    }

    private Fiado findById(Long fiadoId) {
        Optional<Fiado> fiadoOptional = fiadoRepository.findById(fiadoId);
        return fiadoOptional.orElseThrow(() -> new RuntimeException("Fiado nao encontrado"));
    }

    public Fiado save(Fiado fiado) {
        fiado.setValorPendente(calculaValorPendente(fiado));
        setarFiadoIdNoPagamento(fiado, fiado);
        Fiado novoFiado = fiadoRepository.save(fiado);
        criarLancamentoCaixa(novoFiado);
        return novoFiado;
    }

    public Fiado update(Fiado fiado, Long id) {
        Fiado fiadoAntigo = findById(fiado.getFiadoId());
        fiadoAntigo.setFiadoId(id);
        fiadoAntigo.setValorTotal(fiado.getValorTotal());
        fiadoAntigo.setObservacoes(fiado.getObservacoes());
        fiadoAntigo.setPagamento(fiado.getPagamento());
        fiadoAntigo.setCelularCliente(fiado.getCelularCliente());
        fiadoAntigo.setNomeCliente(fiado.getNomeCliente());
        fiadoAntigo.setFiadoId(id);

        Double valorPendenteAntigo = fiadoAntigo.getValorPendente();

        fiadoAntigo.setValorPendente(calculaValorPendente(fiado));
        setarFiadoIdNoPagamento(fiado, fiadoAntigo);

        Fiado fiadoSalvo = fiadoRepository.save(fiadoAntigo);

        criarLancamentoCaixaPagamentoFiado(valorPendenteAntigo - fiadoSalvo.getValorPendente(), fiadoAntigo);
        return fiadoSalvo;
    }

    public void deleteById(Long id) {
        Fiado fiado = findById(id);
        criarLancamentoCaixaEntrada(fiado);
        fiadoRepository.deleteById(id);
    }

    public void setarFiadoIdNoPagamento(Fiado fiado, Fiado fiadoAntigo){
        if (fiado.getPagamento() != null) {
            for (Pagamento pagamento : fiado.getPagamento()) {
                pagamento.setFiado(fiadoAntigo);
            }
        }
    }

    public void criarLancamentoCaixaEntrada(Fiado fiado){
        Caixa lancamentoFiadoCaixa = new Caixa();
        lancamentoFiadoCaixa.setValorTransacao(fiado.getValorPendente());
        lancamentoFiadoCaixa.setTipoTransacao(TipoTransacao.ENTRADA);
        lancamentoFiadoCaixa.setObservacao("Lacamento exclusão fiado de ID: " + fiado.getFiadoId());
        caixaService.save(lancamentoFiadoCaixa);
    }

    public void criarLancamentoCaixa(Fiado fiado){
        Caixa lancamentoFiadoCaixa = new Caixa();
        lancamentoFiadoCaixa.setValorTransacao(fiado.getValorPendente());
        lancamentoFiadoCaixa.setTipoTransacao(TipoTransacao.SAIDA);
        lancamentoFiadoCaixa.setObservacao("Lacamento novo fiado de ID: " + fiado.getFiadoId());
        caixaService.save(lancamentoFiadoCaixa);
    }

    public void criarLancamentoCaixaPagamentoFiado(Double valorPagamento, Fiado fiado){
        Caixa lancamentoFiadoCaixa = new Caixa();
        lancamentoFiadoCaixa.setValorTransacao(valorPagamento);
        lancamentoFiadoCaixa.setTipoTransacao(TipoTransacao.ENTRADA);
        lancamentoFiadoCaixa.setObservacao("Lacamento novo pagamento do fiado de ID: " + fiado.getFiadoId());
        caixaService.save(lancamentoFiadoCaixa);
    }

    private double calculaValorPendente(Fiado fiado) {
        double novoValorPendente;
        double totalPagamentos = 0;
        for(Pagamento pagamentoFiado : fiado.getPagamento()){
            totalPagamentos += pagamentoFiado.getValorPagamento();
        }
        novoValorPendente = (fiado.getValorTotal()-totalPagamentos);
        return novoValorPendente;
    }

    public double obterValorTotalFiado() {
        return fiadoRepository.calcularValorTotalFiado();
    }
}
