package com.luan.controleestoque.service;

import com.luan.controleestoque.model.Caixa;
import com.luan.controleestoque.model.Enum.TipoTransacao;
import com.luan.controleestoque.model.ItemPedido;
import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.repository.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;

    private final ProdutoService produtoService;

    private final CaixaService caixaService;

    @Autowired
    public ItemPedidoService(ItemPedidoRepository itemPedidoRepository, ProdutoService produtoService, CaixaService caixaService) {
        this.itemPedidoRepository = itemPedidoRepository;
        this.produtoService = produtoService;
        this.caixaService = caixaService;
    }

    public ItemPedido findById(Long itemId) {
        Optional<ItemPedido> itemPedidoOptional = itemPedidoRepository.findById(itemId);
        return itemPedidoOptional.orElseThrow(() -> new RuntimeException("Item Pedido nao encontrado"));
    }

    public void deleteById(Long id) {
        Optional<ItemPedido> itemPedido = itemPedidoRepository.findById(id);
        reduzirQuantidadeProdutosEstoque(id, itemPedido.get());
        criarLancamentoCaixa(itemPedido.get());
        itemPedidoRepository.deleteById(id);
    }

    private void reduzirQuantidadeProdutosEstoque(Long id, ItemPedido itemPedido) {
        Produto produto = produtoService.findProdutoByName(itemPedido.getNomeProduto());
        int novaQuantidade = itemPedido.getQuantidade() + produto.getQuantidadeEstoque();
        produto.setQuantidadeEstoque(novaQuantidade);
        produtoService.save(produto);
    }

    public void criarLancamentoCaixa(ItemPedido itemPedido){
        Caixa lancamentoItemPedidoCaixa = new Caixa();
        lancamentoItemPedidoCaixa.setValorTransacao(itemPedido.getValorTotalItem());
        lancamentoItemPedidoCaixa.setTipoTransacao(TipoTransacao.SAIDA);
        lancamentoItemPedidoCaixa.setObservacao("Lancamento de valor exclusao ItemPedido venda ID: " + itemPedido.getVenda().getVendaId());
        caixaService.save(lancamentoItemPedidoCaixa);
    }

}

