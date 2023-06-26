package com.luan.controleestoque.dto;

import com.luan.controleestoque.model.Compra;
import com.luan.controleestoque.model.ItemCompra;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompraDTO {

    private Long compraId;

    private String nomeFornecedor;

    private double valorTotalCompra;

    private List<ItemCompra> itensCompra;

    private LocalDate dataCompra = LocalDate.now();

    public CompraDTO(Compra compra) {
        this.compraId = getCompraId();
        this.nomeFornecedor = getNomeFornecedor();
        this.valorTotalCompra = getValorTotalCompra();
        this.itensCompra = getItensCompra();
        this.dataCompra = getDataCompra();
    }
}
