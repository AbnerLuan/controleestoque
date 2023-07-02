package com.luan.controleestoque.service;

import com.luan.controleestoque.model.ItemCompra;
import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.repository.ItemCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemCompraService {

    private final ItemCompraRepository itemCompraRepository;

    private final ProdutoService produtoService;

    @Autowired
    public ItemCompraService(ItemCompraRepository itemCompraRepository, ProdutoService produtoService) {
        this.itemCompraRepository = itemCompraRepository;
        this.produtoService = produtoService;
    }

    public void deleteById(Long id) {
        Optional<ItemCompra> itemCompra = itemCompraRepository.findById(id);
        reduzirQuantidadeProdutosEstoque(itemCompra.get());
        itemCompraRepository.deleteById(id);
    }

    private void reduzirQuantidadeProdutosEstoque(ItemCompra itemCompra) {
        Produto produto = produtoService.findProdutoByName(itemCompra.getNomeProduto());
        int novaQuantidade = produto.getQuantidadeEstoque() - itemCompra.getQuantidade();
        produto.setQuantidadeEstoque(novaQuantidade);
        produtoService.save(produto);
    }
}
