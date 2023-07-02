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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class CompraService {

    private static final Logger logger = Logger.getLogger(CompraService.class.getName());
    private final CompraRepository compraRepository;
    private final ProdutoService produtoService;

    private final CaixaService caixaService;

    private final ItemCompraService itemCompraService;

    @Autowired
    public CompraService(CompraRepository compraRepository, ProdutoService produtoService, CaixaService caixaService, ItemCompraService itemCompraService) {
        this.compraRepository = compraRepository;
        this.produtoService = produtoService;
        this.caixaService = caixaService;
        this.itemCompraService = itemCompraService;
    }

    public Compra findById(Long id) {
        Optional<Compra> compraOptional = compraRepository.findById(id);
        if (compraOptional.isEmpty()) {
            return compraOptional.orElseThrow(() -> new RuntimeException("Compra nao encontrada!"));
        }
        return compraOptional.get();

    }

    public Page<Compra> findAll(Pageable pageable) {
        return compraRepository.findAll(pageable);
    }


    public Compra save(Compra compra) {
        Map<String, Produto> nomeParaProdutoMap = buscarProdutosPorNome(compra.getItensCompra());
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
            produto.setValorUnitario(item.getValorUnit());
            atualizaProdutoComprado(produto, novaQuantidade);
        });
    }

    public int calcularNovaQuantidade(int quantidadeEstoque, int quantidadeComprada) {
        return quantidadeEstoque + quantidadeComprada;
    }

    public void atualizaProdutoComprado(Produto produto, int novaQuantidade) {
        produto.setQuantidadeEstoque(novaQuantidade);
        produto.setValorTotal(produto.getValorUnitario() * novaQuantidade);
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
        return itemCompra.getQuantidade() * itemCompra.getValorUnit();
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

    public Map<String, Produto> buscarProdutosPorNome(List<ItemCompra> item) {
        Set<String> nomesProdutos = item.stream().map(ItemCompra::getNomeProduto).collect(Collectors.toSet());
        List<Produto> produtos = produtoService.findByNomeProdutoIn(nomesProdutos);

        return produtos.stream().collect(Collectors.toMap(Produto::getNomeProduto, Function.identity()));
    }

    public Compra update(Compra compra, Long id) {
        Compra compraAntiga = findById(id);
        Compra compraAtualizada = new Compra(compra);
        compraAtualizada.setCompraId(id);
        prepararItensUpdate(compraAtualizada, compraAtualizada);
        compraAtualizada.setValorTotalCompra(calcularValorTotalCompra(compraAtualizada.getItensCompra()));
        criarLancamentoCaixaCompraEditada(compraAtualizada, compraAntiga);
        logger.log(Level.INFO, "Compra atualizada com sucesso.");
        return compraRepository.save(compraAtualizada);
    }

    private void prepararItensUpdate(Compra compraNova, Compra compraAtualizada) {
        List<ItemCompra> listaItensAtualizados = compraNova.getItensCompra();

        Map<String, Produto> nomeParaProdutoMap = buscarProdutosPorNome(compraNova.getItensCompra());

        for (ItemCompra itemAtualizado : listaItensAtualizados) {
            Produto produto = nomeParaProdutoMap.get(itemAtualizado.getNomeProduto());
            if (produto != null) {
                itemAtualizado.setProduto(produto);
                itemAtualizado.setCompra(compraAtualizada);
                itemAtualizado.calcularValorTotalItem();
            }
        }

    }

    public void criarLancamentoCaixaCompraEditada(Compra compraaAtualizada, Compra compraAntiga) {
        Caixa lancamentoVendaCaixa = new Caixa();
        lancamentoVendaCaixa.setValorTransacao(calculaValorCompraLiquidoUpdate(compraaAtualizada, compraAntiga));
        if (lancamentoVendaCaixa.getValorTransacao() != 0) {
            if (lancamentoVendaCaixa.getValorTransacao() > 0) {
                lancamentoVendaCaixa.setTipoTransacao(TipoTransacao.ENTRADA);
            } else {
                lancamentoVendaCaixa.setTipoTransacao(TipoTransacao.SAIDA);
                lancamentoVendaCaixa.setValorTransacao(lancamentoVendaCaixa.getValorTransacao() * -1);
            }
            lancamentoVendaCaixa.setObservacao("Lancamento Edição compra de ID: " + compraaAtualizada.getCompraId());
            logger.log(Level.INFO, "Lancamento Edição compra de ID: " + compraaAtualizada.getCompraId());
            caixaService.save(lancamentoVendaCaixa);
        }
    }

    public double calculaValorCompraLiquidoUpdate(Compra Atualizada, Compra Antiga) {
        double valorTransacao = 0;
        if (Atualizada.getValorTotalCompra() == 0) {
            valorTransacao = (Antiga.getValorTotalCompra());
        } else {
            valorTransacao = Antiga.getValorTotalCompra() - Atualizada.getValorTotalCompra();
        }
        return valorTransacao;
    }

    public void deleteById(Long id) {
        Optional<Compra> optionalVenda = compraRepository.findById(id);
        if (optionalVenda.isPresent()) {
            Compra compra = optionalVenda.get();
            List<ItemCompra> itensPedido = compra.getItensCompra();
            for (ItemCompra itemCompra : itensPedido) {
                itemCompraService.deleteById(itemCompra.getItemCompraId());
            }
            criarLancamentoCaixaExclusao(compra);
            compraRepository.deleteById(id);
            logger.log(Level.INFO, "Compra Deletada de ID: " + id);
        } else {
            throw new RuntimeException("Venda não encontrada");
        }
    }

    public void criarLancamentoCaixaExclusao(Compra compra) {
        Caixa lancamentoVendaCaixa = new Caixa();
        lancamentoVendaCaixa.setValorTransacao(compra.getValorTotalCompra());
        lancamentoVendaCaixa.setTipoTransacao(TipoTransacao.ENTRADA);
        lancamentoVendaCaixa.setObservacao("(+)Lancamento exclusao compra de ID: " + compra.getCompraId());
        logger.log(Level.INFO, "(+)Lancamento Exclusao compra de ID: " + compra.getCompraId());
        caixaService.save(lancamentoVendaCaixa);
    }
}

