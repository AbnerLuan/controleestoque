package com.luan.controleestoque.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    private LocalDate dataVenda = LocalDate.now();

    @OneToMany(mappedBy = "venda", cascade = {CascadeType.ALL})
    private List<ItemPedido> itens;

    public Venda(Long vendaId, String nomeCliente, String canalVenda, double valorTotalVenda, LocalDate dataVenda) {
        this.vendaId = vendaId;
        this.nomeCliente = nomeCliente;
        this.canalVenda = canalVenda;
        this.valorTotalVenda = valorTotalVenda;
        this.dataVenda = dataVenda;
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
