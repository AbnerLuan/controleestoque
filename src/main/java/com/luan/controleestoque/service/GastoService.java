package com.luan.controleestoque.service;

import com.luan.controleestoque.model.Gasto;
import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.repository.GastoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class GastoService {

    private final GastoRepository gastoRepository;
    private static final Logger logger = Logger.getLogger(GastoService.class.getName());

    public GastoService(GastoRepository gastoRepository){
        this.gastoRepository = gastoRepository;
    }

    public Page<Gasto> findAllPageable(Pageable pageable) {
        logger.log(Level.INFO, "Lista de Gastos Carregada.");
        return gastoRepository.findAll(pageable);
    }


    public Gasto save(Gasto gasto) {
        logger.log(Level.INFO, "Novo Gasto Salvo: " + gasto.getDescricao());
        return gastoRepository.save(gasto);
    }

    public void deleteById(Long id) {
        logger.log(Level.INFO, "Gasto excluido com sucesso! id: " + id);
        gastoRepository.deleteById(id);
    }
}
