package com.luan.controleestoque.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Gasto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gastoId;

    @NotNull(message = "Campo descricao é obrigatório")
    private String descricao;

    @NotNull(message = "Campo valor é obrigatório")
    private Double valor;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataRegistro = LocalDate.now();

}
