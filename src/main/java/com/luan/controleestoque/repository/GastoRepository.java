package com.luan.controleestoque.repository;

import com.luan.controleestoque.model.Gasto;
import com.luan.controleestoque.model.MesValor;
import com.luan.controleestoque.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {

    @Query("SELECT COALESCE(SUM(f.valor), 0) FROM Gasto f")
    double calculaValorTotalGastoAnual();

    @Query("SELECT COALESCE(SUM(f.valor), 0) FROM Gasto f WHERE MONTH(f.dataRegistro) = MONTH(CURRENT_DATE) AND YEAR(f.dataRegistro) = YEAR(CURRENT_DATE)")
    double calcularGastoMesAtual();

    @Query("SELECT NEW com.luan.controleestoque.model.MesValor(MONTH(f.dataRegistro), COALESCE(SUM(f.valor), 0)) FROM Gasto f WHERE YEAR(f.dataRegistro) = YEAR(CURRENT_DATE) GROUP BY MONTH(f.dataRegistro) ORDER BY MONTH(f.dataRegistro)")
    List<MesValor> getValoresGastoMensalFromDatabase();

}
