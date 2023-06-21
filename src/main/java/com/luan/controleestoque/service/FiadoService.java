package com.luan.controleestoque.service;

import com.luan.controleestoque.model.Fiado;
import com.luan.controleestoque.repository.FiadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class FiadoService {

    private final FiadoRepository fiadoRepository;
    private static final Logger logger = Logger.getLogger(GastoService.class.getName());

    public FiadoService(FiadoRepository fiadoRepository){
        this.fiadoRepository = fiadoRepository;
    }

    public List<Fiado> findAll() {
        return fiadoRepository.findAll();
    }

    public Fiado save(Fiado fiado) {
        return fiadoRepository.save(fiado);
    }
    public void deleteById(Long id) {
        fiadoRepository.deleteById(id);
    }
}
