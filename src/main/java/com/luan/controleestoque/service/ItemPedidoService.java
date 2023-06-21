package com.luan.controleestoque.service;

import com.luan.controleestoque.model.ItemPedido;
import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.repository.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;

    private final ProdutoService produtoService;

    @Autowired
    public ItemPedidoService(ItemPedidoRepository itemPedidoRepository, ProdutoService produtoService) {
        this.itemPedidoRepository = itemPedidoRepository;
        this.produtoService = produtoService;
    }

    public ItemPedido findById(Long itemId) {
        Optional<ItemPedido> itemPedidoOptional = itemPedidoRepository.findById(itemId);
        return itemPedidoOptional.orElseThrow(() -> new RuntimeException("Item Pedido nao encontrado"));
    }

    public void deleteById(Long id) {
        reduzirQuantidadeProdutosEstoque(id);
        itemPedidoRepository.deleteById(id);
    }

    private void reduzirQuantidadeProdutosEstoque(Long id) {
        Optional<ItemPedido> itemPedido = itemPedidoRepository.findById(id);
        Produto produto = produtoService.findProdutoByName(itemPedido.get().getNomeProduto());
        int novaQuantidade = itemPedido.get().getQuantidade() + produto.getQuantidadeEstoque();
        produto.setQuantidadeEstoque(novaQuantidade);
        produtoService.save(produto);

    }

}

