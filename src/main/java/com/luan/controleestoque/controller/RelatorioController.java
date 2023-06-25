package com.luan.controleestoque.controller;

import com.luan.controleestoque.model.Relatorio;
import com.luan.controleestoque.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/relatorios")
@RestController
public class RelatorioController {

    private final RelatorioService relatorioService;

    @Autowired
    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }
    @GetMapping
    public ResponseEntity<Relatorio> getRelatorio() {
        Relatorio relatorio = relatorioService.gerarRelatorio();
        return ResponseEntity.ok().body(relatorio);
    }
}
