package com.luan.controleestoque.service;

import com.luan.controleestoque.model.ItemPedido;
import com.luan.controleestoque.model.Venda;
import com.luan.controleestoque.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;

    public List<Venda> findAll() {
        List<Venda> vendas = vendaRepository.findAll();
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


    @Autowired
    public VendaService(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }


    public Venda findById(Long id) {
        Optional<Venda> vendaOptional = vendaRepository.findById(id);
        return vendaOptional.orElseThrow(() -> new RuntimeException("Venda nao encontrada"));
    }

    public void deleteById(Long id) {
        vendaRepository.deleteById(id);
    }

    public Venda save(Venda venda) {
        Venda vendaSalva = new Venda(venda);
        vendaSalva.setDataVenda(LocalDate.now());

        for (ItemPedido item : venda.getItens()) {
            item.setVenda(vendaSalva);
            item.calcularValorTotalItem();
        }
        vendaSalva.setItens(venda.getItens());
        vendaRepository.save(vendaSalva);
        return vendaSalva;
    }

    public Venda update(Venda venda, Long id) {
        Venda vendaAntiga = findById(id);
        vendaAntiga.setItens(venda.getItens());

        for (ItemPedido item : vendaAntiga.getItens()) {
            item.setVenda(vendaAntiga);
        }

        // Mantém a data original da venda antiga
        vendaAntiga.setDataVenda(vendaAntiga.getDataVenda());

        return vendaRepository.save(vendaAntiga);
    }

}


