package com.luan.controleestoque.service;

import com.luan.controleestoque.model.Venda;
import com.luan.controleestoque.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;

    public List<Venda> findAll() {return vendaRepository.findAll();}

    @Autowired
    public VendaService(VendaRepository vendaRepository){this.vendaRepository = vendaRepository;}


    public Venda findById(Long id) {
        Optional<Venda> vendaOptional = vendaRepository.findById(id);
        return vendaOptional.orElseThrow(() -> new RuntimeException("Venda nao encontrada"));
    }

    public void deleteById(Long id) {vendaRepository.deleteById(id);};

    public Venda save(Venda venda) {return vendaRepository.save(venda);}


    public Venda update(Venda venda, Long id) {
        Venda novaVenda = findById(venda.getVendaId());
        novaVenda.setCanalVenda(venda.getCanalVenda());
        novaVenda.setDataVenda(venda.getDataVenda());
        novaVenda.setValorTotalVenda(venda.getValorTotalVenda());
        novaVenda.setNomeCliente(venda.getNomeCliente());
        novaVenda.setItens(venda.getItens());

        return vendaRepository.save(venda);
    }
}


