package com.luan.controleestoque.service;

import com.luan.controleestoque.model.ItemPedido;
import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.model.Venda;
import com.luan.controleestoque.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoService produtoService;
    private static final Logger logger = Logger.getLogger(VendaService.class.getName());


    @Autowired
    public VendaService(VendaRepository vendaRepository, ProdutoService produtoService) {
        this.vendaRepository = vendaRepository;
        this.produtoService = produtoService;
    }

    public Page<Venda> findAll(Pageable pageable) {
        Page<Venda> vendas = vendaRepository.findAll(pageable);
        for (Venda venda : vendas) {
            calcularValorTotalVenda(venda);
        }
        logger.log(Level.INFO, "Lista de vendas carregada.");
        return vendas;
    }

    public Venda findById(Long id) {
        Optional<Venda> vendaOptional = vendaRepository.findById(id);
        return vendaOptional.orElseThrow(() -> new RuntimeException("Venda nao encontrada"));
    }

    public Venda save(Venda venda) {
        Venda vendaSalva = new Venda(venda);
        vendaSalva.setDataVenda(LocalDate.now());

        if (!verificarQuantidadeProdutos(venda.getItens())) {
            throw new RuntimeException("Quantidade insuficiente de produtos no estoque.");
        }

        List<ItemPedido> itensAtualizados = prepararItensSave(venda.getItens(), vendaSalva);
        reduzirQuantidadeProdutosEstoque(itensAtualizados);
        vendaSalva.setItens(itensAtualizados);
        vendaRepository.save(vendaSalva);
        logger.log(Level.INFO, "Venda Salva com sucesso.");
        return vendaSalva;
    }

    public Venda update(Long id, Venda venda) {
        Venda vendaAntiga = findById(id);
        vendaAntiga.setVendaId(id);
        vendaAntiga.setItens(prepararItensUpdate(venda.getItens(), vendaAntiga));
        vendaAntiga.setDataVenda(venda.getDataVenda());
        logger.log(Level.INFO, "Venda atualizada com sucesso.");
        return vendaRepository.save(vendaAntiga);
    }
    public void deleteById(Long id) {
        vendaRepository.deleteById(id);
    }

    private void calcularValorTotalVenda(Venda venda) {
        if (venda == null || venda.getItens().isEmpty()) {
            return;
        }
        double total = venda.getItens().stream()
                .filter(Objects::nonNull)
                .mapToDouble(ItemPedido::getValorTotalItem)
                .sum();
        venda.setValorTotalVenda(total);
    }

    private List<ItemPedido> prepararItensUpdate(List<ItemPedido> itensNovos, Venda vendaAntiga) {
        List<ItemPedido> itensVelhos = vendaAntiga.getItens();

        return itensNovos.stream()
                .map(itemNovo -> {
                    ItemPedido itemAntigo = encontrarItemAntigo(itemNovo, itensVelhos);
                    if (itemAntigo != null) {
                        atualizarItemAntigo(itemAntigo, itemNovo);
                        return itemAntigo;
                    } else {
                        configurarNovoItemVenda(itemNovo);
                        itemNovo.setVenda(vendaAntiga);
                        itemNovo.calcularValorTotalItem();
                        return itemNovo;
                    }
                })
                .collect(Collectors.toList());
    }

    private ItemPedido encontrarItemAntigo(ItemPedido itemNovo, List<ItemPedido> itensVelhos) {
        return itensVelhos.stream()
                .filter(item -> item.getItemId() != null && item.getItemId().equals(itemNovo.getItemId()))
                .findFirst()
                .orElse(null);
    }

    private void atualizarItemAntigo(ItemPedido itemAntigo, ItemPedido itemNovo) {
        if (!itemAntigo.equals(itemNovo)) {
            itemAntigo.setNomeProduto(itemNovo.getNomeProduto());
            itemAntigo.setQuantidade(itemNovo.getQuantidade());
            itemAntigo.setValorUnit(itemNovo.getValorUnit());
            itemAntigo.calcularValorTotalItem();
        }
    }

    private void configurarNovoItemVenda(ItemPedido itemNovo) {
        if (itemNovo.getProduto() != null && itemNovo.getProduto().getProdutoId() != null) {
            // O itemNovo já possui o ID do produto, não é necessário fazer mais nada
        } else {
            Long produtoId = produtoService.findIdByNomeProduto(itemNovo.getNomeProduto());
            if (produtoId != null) {
                Produto produto = new Produto();
                produto.setProdutoId(produtoId);
                itemNovo.setProduto(produto);
            } else {
                throw new RuntimeException("O produto com nome " + itemNovo.getNomeProduto() + " não foi encontrado.");
            }
        }
    }

    private List<ItemPedido> prepararItensSave(List<ItemPedido> itens, Venda vendaSalva) {

        List<Produto> produtos = produtoService.findAll(); //Buscar todos os produtos

        Map<String, Long> nomeParaProdutoIdMap = produtos.stream()  //Criar um mapa para mapear o nome do produto ao seu ID
                .collect(Collectors.toMap(Produto::getNomeProduto, Produto::getProdutoId));

        return itens.stream() // Processar cada item
                .peek(item -> item.setVenda(vendaSalva)) //Configurar a venda para cada item
                .peek(item -> {
                    Long produtoId = nomeParaProdutoIdMap.get(item.getNomeProduto()); //Obter o ID do produto pelo nome
                    if (produtoId != null) {
                        Produto produto = new Produto();
                        produto.setProdutoId(produtoId);
                        item.setProduto(produto); //Configurar o produto no item
                    }
                    item.calcularValorTotalItem(); //Calcular o valor total do item
                })
                .collect(Collectors.toList()); //Coletar os itens atualizados em uma lista
    }

    private boolean verificarQuantidadeProdutos(List<ItemPedido> itens) {
        for (ItemPedido item : itens) {
            Produto produto = produtoService.findProdutoByName(item.getNomeProduto());
            if (produto != null && item.getQuantidade() > produto.getQuantidadeEstoque()) {
                return false;
            }
        }
        return true;
    }

    private void reduzirQuantidadeProdutosEstoque(List<ItemPedido> itens) {
        for (ItemPedido item : itens) {
            Produto produto = produtoService.findProdutoByName(item.getNomeProduto());
            int novaQuantidade = produto.getQuantidadeEstoque() - item.getQuantidade();
            produto.setQuantidadeEstoque(novaQuantidade);
            produtoService.save(produto);
        }
    }

}


