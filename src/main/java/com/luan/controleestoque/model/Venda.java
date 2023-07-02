package com.luan.controleestoque.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.luan.controleestoque.model.Enum.CanalVenda;
import jakarta.persistence.*;
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

    private CanalVenda canalVenda;

    private double valorTotalVenda;

    @JsonFormat(pattern = "dd/MM/yyyy")
    protected LocalDate dataVenda = LocalDate.now();

    private double valorFrete;

    private double valorTarifa;

    private double lucroVenda;

    @OneToMany(fetch= FetchType.LAZY, cascade= CascadeType.ALL, mappedBy = "venda")
    private List<ItemPedido> itens;

    public Venda(Long vendaId, String nomeCliente, CanalVenda canalVenda, double valorTotalVenda, LocalDate dataVenda,
                 double valorFrete, double valorTarifa, double lucroVenda) {
        this.vendaId = vendaId;
        this.nomeCliente = nomeCliente;
        this.canalVenda = canalVenda;
        this.valorTotalVenda = valorTotalVenda;
        this.dataVenda = LocalDate.now();
        this.valorFrete = valorFrete;
        this.valorTarifa = valorTarifa;
        this.lucroVenda = lucroVenda;
    }

    public Venda(Venda venda) {
        this.nomeCliente = venda.getNomeCliente();
        this.canalVenda = venda.getCanalVenda();
        this.valorTotalVenda = venda.getValorTotalVenda();
        this.dataVenda = venda.getDataVenda();
        this.valorFrete = venda.getValorFrete();
        this.valorTarifa = venda.getValorTarifa();
        this.lucroVenda = venda.getLucroVenda();
        this.itens = venda.getItens();
    }

}
