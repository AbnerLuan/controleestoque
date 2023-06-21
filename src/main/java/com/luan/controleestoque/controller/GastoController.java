package com.luan.controleestoque.controller;

import com.luan.controleestoque.model.Gasto;
import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.service.GastoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/gastos")
public class GastoController {
    private final GastoService gastoService;
    @Autowired
    public GastoController(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    @GetMapping
    public ResponseEntity<Page<Gasto>> findAll(@PageableDefault(sort = "gastoId",
            direction = Sort.Direction.DESC,
            page = 0,
            size = 10) Pageable pageable){
        Page<Gasto> gastoPage = gastoService.findAllPageable(pageable);
        return ResponseEntity.ok().body(gastoPage);
    }

    @PostMapping
    public ResponseEntity<Gasto> create(@RequestBody @Valid Gasto gasto){
        Gasto newObj = gastoService.save(gasto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getGastoId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Gasto> deleteById(@PathVariable @Valid Long id) {
        gastoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
