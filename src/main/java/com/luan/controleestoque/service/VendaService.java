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
import java.util.Optional;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoService produtoService;

    private final ItemPedidoService itemPedidoService;

    @Autowired
    public VendaService(VendaRepository vendaRepository, ProdutoService produtoService,
                        ItemPedidoService itemPedidoService) {
        this.vendaRepository = vendaRepository;
        this.produtoService = produtoService;
        this.itemPedidoService = itemPedidoService;
    }

    public Page<Venda> findAll(Pageable pageable) {
        Page<Venda> vendas = vendaRepository.findAll(pageable);
        for (Venda venda : vendas) {
            calcularValorTotalVenda(venda);
        }
        return vendas;
    }

    private void calcularValorTotalVenda(Venda venda) {
        if (venda == null || venda.getItens() == null) {
            return;
        }

        double total = 0.0;
        for (ItemPedido item : venda.getItens()) {
            if (item != null) {
                total += item.getValorTotalItem();
            }
        }
        venda.setValorTotalVenda(total);
    }

    public Venda findById(Long id) {
        Optional<Venda> vendaOptional = vendaRepository.findById(id);
        return vendaOptional.orElseThrow(() -> new RuntimeException("CanalVenda nao encontrada"));
    }

    public void deleteById(Long id) {
        vendaRepository.deleteById(id);
    }

    public Venda save(Venda venda) {
        Venda vendaSalva = new Venda(venda);
        vendaSalva.setDataVenda(LocalDate.now());
        List<ItemPedido> itensAtualizados = prepararItensSave(venda.getItens(), vendaSalva);
        vendaSalva.setItens(itensAtualizados);
        vendaRepository.save(vendaSalva);
        return vendaSalva;
    }


    public Venda update(Long id, Venda venda) {
        Venda vendaAntiga = findById(id);
        vendaAntiga = new Venda(venda);
        vendaAntiga.setVendaId(id);
        List<ItemPedido> itensAtualizados = prepararItens(venda.getItens(), vendaAntiga);
        vendaAntiga.setItens(itensAtualizados);
        vendaAntiga.setDataVenda(vendaAntiga.getDataVenda());
        return vendaRepository.save(vendaAntiga);
    }

    private List<ItemPedido> prepararItens(List<ItemPedido> itensNovos, Venda vendaAntiga) {
        List<ItemPedido> itensAtualizados = new ArrayList<>();

        for (ItemPedido itemNovo : itensNovos) {
            ItemPedido itemAntigo = null;

            for (ItemPedido item : vendaAntiga.getItens()) {
                if (item.getItemId() != null && item.getItemId().equals(itemNovo.getItemId())) {
                    itemAntigo = item;
                    break;
                }
            }

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

        List<ItemPedido> itensAntigos = new ArrayList<>(vendaAntiga.getItens());
        itensAntigos.removeAll(itensAtualizados);
        for (ItemPedido itemAntigo : itensAntigos) {
            itemPedidoService.delete(itemAntigo);
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

}


