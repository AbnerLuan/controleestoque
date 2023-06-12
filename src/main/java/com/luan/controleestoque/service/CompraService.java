package com.luan.controleestoque.service;

import com.luan.controleestoque.model.Compra;
import com.luan.controleestoque.model.ItemCompra;
import com.luan.controleestoque.model.ItemPedido;
import com.luan.controleestoque.repository.CompraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompraService {

    private final CompraRepository compraRepository;

    public CompraService (CompraRepository compraRepository) {this.compraRepository = compraRepository;}

    public Compra findById(Long id) {
        Optional<Compra> compraOptional = compraRepository.findById(id);
        if(compraOptional.isEmpty()){
        return compraOptional.orElseThrow(() -> new RuntimeException("Compra nao encontrada!"));
        }
        return compraOptional.get();

    }

    public List<Compra> findAll() {
        return compraRepository.findAll();}


    public Compra save(Compra compra) {return compraRepository.save(compra);}

    public Compra update(Compra compra, Long id) {
        compra.setCompraId(id);
        Compra compraAntiga = findById(id);
        compraAntiga= new Compra(compra);
        for (ItemCompra item : compra.getItensCompra()) {
            item.setCompra(compraAntiga);
        }
        compraAntiga.setItensCompra(compra.getItensCompra());

        return compraRepository.save(compraAntiga);
    }

    public void deleteById(Long id) {compraRepository.deleteById(id);}
}

