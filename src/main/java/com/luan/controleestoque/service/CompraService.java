package com.luan.controleestoque.service;

import com.luan.controleestoque.model.Caixa;
import com.luan.controleestoque.model.Compra;
import com.luan.controleestoque.model.Enum.TipoTransacao;
import com.luan.controleestoque.model.ItemCompra;
import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final ProdutoService produtoService;

    private final CaixaService caixaService;

    @Autowired
    public CompraService (CompraRepository compraRepository, ProdutoService produtoService, CaixaService caixaService) {
        this.compraRepository = compraRepository;
        this.produtoService = produtoService;
        this.caixaService = caixaService;
    }

    public Compra findById(Long id) {
        Optional<Compra> compraOptional = compraRepository.findById(id);
        if(compraOptional.isEmpty()){
        return compraOptional.orElseThrow(() -> new RuntimeException("Compra nao encontrada!"));
        }
        return compraOptional.get();

    }

    public Page<Compra> findAll(Pageable pageable) {
        return compraRepository.findAll(pageable);}


    public Compra save(Compra compra) {
        Map<String, Produto> nomeParaProdutoMap = buscarProdutoPorNome(compra.getItensCompra());
        compra.setItensCompra(prepararItemSave(compra, nomeParaProdutoMap));
        compra.setValorTotalCompra(calcularValorTotalCompra(compra.getItensCompra()));
        compra.setDataCompra(LocalDate.now());
        atualizarQuantidadeDeItensEmEstoque(compra.getItensCompra());
        compraRepository.save(compra);
        criarLancamentoCaixa(compra);

        return compra;
    }

    public void atualizarQuantidadeDeItensEmEstoque(List<ItemCompra> itens) {
        itens.forEach(item -> {
           Produto produto = item.getProduto();
           int novaQuantidade = calcularNovaQuantidade(produto.getQuantidadeEstoque(), item.getQuantidade());
           atualizarQuantidadeProdutoEstoque(produto, novaQuantidade);
        });
    }

    public int calcularNovaQuantidade(int quantidadeEstoque, int quantidadeComprada) {
        return quantidadeEstoque + quantidadeComprada;
    }

    public void atualizarQuantidadeProdutoEstoque(Produto produto, int novaQuantidade) {
        produto.setQuantidadeEstoque(novaQuantidade);
        produtoService.save(produto);

    }
    public List<ItemCompra> prepararItemSave(Compra compra, Map<String, Produto> nomeParaProdutoMap) {
        List<ItemCompra> itens = compra.getItensCompra();

        return itens.stream().peek(item -> item.setCompra(compra))
                .peek(item -> item.setProduto(nomeParaProdutoMap.get(item.getNomeProduto())))
                .peek(item -> item.setValorTotalItem(calcularValorTotalItemCompra(item)))
                .collect(Collectors.toList());
    }

    public Double calcularValorTotalItemCompra(ItemCompra itemCompra) {
        return itemCompra.getQuantidade()*itemCompra.getValorUnit();
    }

    public Double calcularValorTotalCompra(List<ItemCompra> itemCompra) {
        return itemCompra.stream().mapToDouble(ItemCompra::getValorTotalItem).sum();
    }

    public void criarLancamentoCaixa(Compra compra) {
        Caixa lancamentoCompraCaixa = new Caixa();
        lancamentoCompraCaixa.setTipoTransacao(TipoTransacao.SAIDA);
        lancamentoCompraCaixa.setObservacao("Lancamento Compra id: " + compra.getCompraId());
        lancamentoCompraCaixa.setValorTransacao(compra.getValorTotalCompra());
        caixaService.save(lancamentoCompraCaixa);
    }

    public Map<String, Produto> buscarProdutoPorNome(List<ItemCompra> item) {
        Set<String> nomesProdutos = item.stream().map(ItemCompra::getNomeProduto).collect(Collectors.toSet());
        List<Produto> produtos = produtoService.findByNomeProdutoIn(nomesProdutos);

        return produtos.stream().collect(Collectors.toMap(Produto::getNomeProduto, Function.identity()));
    }

    public Compra update(Compra compra, Long id) {
        compra.setCompraId(id);
        //Compra compraAntiga = findById(id);
       Compra compraAntiga = new Compra(compra);
        for (ItemCompra item : compra.getItensCompra()) {
            item.setCompra(compraAntiga);
            item.setItemCompraId(id);
        }
        compraAntiga.setItensCompra(compra.getItensCompra());
        compraAntiga.setNomeFornecedor(compra.getNomeFornecedor());

        return compraRepository.save(compraAntiga);
    }

    public void deleteById(Long id) {compraRepository.deleteById(id);}
}

