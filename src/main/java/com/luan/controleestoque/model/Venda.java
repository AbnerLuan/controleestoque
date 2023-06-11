package com.luan.controleestoque.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vendaId;

    private String nomeCliente;

    private String canalVenda;

    private double valorTotalVenda;

    @JsonFormat(pattern = "dd/MM/yyyy")
    protected LocalDate dataVenda = LocalDate.now();

    @OneToMany(fetch= FetchType.LAZY, cascade= CascadeType.ALL, mappedBy = "venda")
    private List<ItemPedido> itens;

    public Venda(Long vendaId, String nomeCliente, String canalVenda, double valorTotalVenda, LocalDate dataVenda) {
        this.vendaId = vendaId;
        this.nomeCliente = nomeCliente;
        this.canalVenda = canalVenda;
        this.valorTotalVenda = valorTotalVenda;
        this.dataVenda = LocalDate.now();
    }

    public Venda(Venda venda) {
        this.vendaId = venda.getVendaId();
        this.nomeCliente = venda.getNomeCliente();
        this.canalVenda = venda.getCanalVenda();
        this.valorTotalVenda = venda.getValorTotalVenda();
        this.dataVenda = venda.getDataVenda();
        this.itens = venda.getItens();
    }

    public Venda(Venda venda) {
        this.vendaId = venda.getVendaId();
        this.nomeCliente = venda.getNomeCliente();
        this.canalVenda = venda.getCanalVenda();
        this.valorTotalVenda = venda.getValorTotalVenda();
        this.dataVenda = venda.getDataVenda();
        this.itens = venda.getItens();
    }
}
