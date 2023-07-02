package com.luan.controleestoque.service;

import com.luan.controleestoque.model.Caixa;
import com.luan.controleestoque.model.Enum.TipoTransacao;
import com.luan.controleestoque.model.Pagamento;
import com.luan.controleestoque.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PagamentoService {

    private static final Logger logger = Logger.getLogger(GastoService.class.getName());
    private final PagamentoRepository pagamentoRepository;

    private final CaixaService caixaService;
    @Autowired
    public PagamentoService(PagamentoRepository pagamentoRepository, CaixaService caixaService) {
        this.pagamentoRepository = pagamentoRepository;
        this.caixaService = caixaService;
    }

    public Optional<Pagamento> findById(Long id) {
        return pagamentoRepository.findById(id);
    }

    public Pagamento save(Pagamento pagamento) {
        logger.log(Level.INFO, "Pagamento Salvo");
        return pagamentoRepository.save(pagamento);
    }

    public void deleteById(Long id) {
        Optional<Pagamento> pagamentoOptional = findById(id);
        if (pagamentoOptional.isPresent()) {
            Pagamento pagamento = pagamentoOptional.get();
            criarLancamentoCaixa(pagamento);
            logger.log(Level.INFO, "Pagamento deletado " + id);
            pagamentoRepository.deleteById(id);
        }
    }

    private void criarLancamentoCaixa(Pagamento pagamento){
        Caixa lancamentoPagamentoCaixa = new Caixa();
        lancamentoPagamentoCaixa.setValorTransacao(pagamento.getValorPagamento());
        lancamentoPagamentoCaixa.setTipoTransacao(TipoTransacao.SAIDA);
        lancamentoPagamentoCaixa.setObservacao("Lancamento exclusao pagamento do fiado ID: " + pagamento.getFiado().getFiadoId());
        caixaService.save(lancamentoPagamentoCaixa);
    }

}
