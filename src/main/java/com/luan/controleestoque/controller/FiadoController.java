package com.luan.controleestoque.controller;

import com.luan.controleestoque.model.Fiado;
import com.luan.controleestoque.service.FiadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/fiados")
public class FiadoController {

    private final FiadoService fiadoService;
    @Autowired
    public FiadoController(FiadoService fiadoService) {
        this.fiadoService = fiadoService;
    }

    @GetMapping
    public ResponseEntity<List<Fiado>> findAll(){
        List<Fiado> fiadoList = fiadoService.findAll();
        fiadoList.sort(Comparator.comparingLong(Fiado::getFiadoId).reversed());
        return ResponseEntity.ok().body(fiadoList);
    }

    @PostMapping
    public ResponseEntity<Fiado> create(@RequestBody @Valid Fiado fiado){
        Fiado newObj = fiadoService.save(fiado);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getFiadoId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Fiado> deleteById(@PathVariable @Valid Long id) {
        fiadoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
