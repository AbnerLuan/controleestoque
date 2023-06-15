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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        vendaAntiga.setItens(prepararItens(venda.getItens(), vendaAntiga));
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

    private List<ItemPedido> prepararItens(List<ItemPedido> itensNovos, Venda vendaAntiga) {
        List<ItemPedido> itensAtualizados = new ArrayList<>();
        List<ItemPedido> itensVelhos = vendaAntiga.getItens();

        if (!itensNovos.isEmpty()) {
            if (!itensVelhos.isEmpty()) {
                List<ItemPedido> itensExcluir = new ArrayList<>(itensVelhos);
                itensExcluir.removeIf(itemVelho -> itensNovos.stream().anyMatch(itemNovo -> itemNovo.getItemId() != null && itemNovo.getItemId().equals(itemVelho.getItemId())));
            }

            for (ItemPedido itemNovo : itensNovos) {
                ItemPedido itemAntigo = itensVelhos.stream().filter(item -> item.getItemId() != null && item.getItemId().equals(itemNovo.getItemId())).findFirst().orElse(null);

                if (itemAntigo != null) {
                    if (!itemAntigo.equals(itemNovo)) {
                        itemAntigo.setNomeProduto(itemNovo.getNomeProduto());
                        itemAntigo.setQuantidade(itemNovo.getQuantidade());
                        itemAntigo.setValorUnit(itemNovo.getValorUnit());

                        itemAntigo.calcularValorTotalItem();
                    }
                    itensAtualizados.add(itemAntigo);
                } else {
                    itemNovo.setVenda(vendaAntiga);
                    itemNovo.calcularValorTotalItem();
                    itensAtualizados.add(itemNovo);
                }
            }
        }

        return itensAtualizados;
    }


    private List<ItemPedido> prepararItensSave(List<ItemPedido> itens, Venda vendaSalva) {
        List<ItemPedido> itensAtualizados = new ArrayList<>();

        for (ItemPedido item : itens) {
            item.setVenda(vendaSalva);

            Long produtoId = produtoService.findIdByNomeProduto(item.getNomeProduto());

            if (produtoId != null) {
                Produto produto = new Produto();
                produto.setProdutoId(produtoId);
                item.setProduto(produto);
            }

            item.calcularValorTotalItem();
            itensAtualizados.add(item);
        }

        return itensAtualizados;
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


