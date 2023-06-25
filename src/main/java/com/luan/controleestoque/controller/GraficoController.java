package com.luan.controleestoque.controller;

import com.luan.controleestoque.model.Grafico;
import com.luan.controleestoque.service.GraficoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/grafico")
public class GraficoController {
    private final GraficoService graficoService;
    @Autowired
    public GraficoController(GraficoService graficoService) {
        this.graficoService = graficoService;
    }

    @GetMapping
    public ResponseEntity<List<Grafico>> getDadosGrafico() {
        List<Grafico> dadosGrafico = graficoService.getDadosGrafico();
        return ResponseEntity.ok().body(dadosGrafico);
    }
}
