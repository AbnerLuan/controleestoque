package com.luan.controleestoque.repository;

import com.luan.controleestoque.model.MesValor;
import com.luan.controleestoque.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query("SELECT COALESCE(SUM(f.valorTotalVenda), 0) FROM Venda f")
    double calculaValorTotalFaturamentoAnual();

    @Query("SELECT COALESCE(SUM(f.valorTotalVenda), 0) FROM Venda f WHERE MONTH(f.dataVenda) = MONTH(CURRENT_DATE) AND YEAR(f.dataVenda) = YEAR(CURRENT_DATE)")
    double calculaValorTotalFaturamentoMesAtual();

    @Query("SELECT COALESCE(SUM(f.lucroVenda), 0) FROM Venda f")
    double calculaValorTotalLucroAnual();

    @Query("SELECT COALESCE(SUM(f.lucroVenda), 0) FROM Venda f WHERE MONTH(f.dataVenda) = MONTH(CURRENT_DATE) AND YEAR(f.dataVenda) = YEAR(CURRENT_DATE)")
    double calculaValorTotalLucroMesAtual();

    @Query("SELECT NEW com.luan.controleestoque.model.MesValor(MONTH(f.dataVenda), COALESCE(SUM(f.valorTotalVenda), 0)) FROM Venda f WHERE YEAR(f.dataVenda) = YEAR(CURRENT_DATE) GROUP BY MONTH(f.dataVenda) ORDER BY MONTH(f.dataVenda)")
    List<MesValor> getValoresFaturamentoMensal();


    @Query("SELECT NEW com.luan.controleestoque.model.MesValor(MONTH(f.dataVenda), COALESCE(SUM(f.lucroVenda), 0)) FROM Venda f WHERE YEAR(f.dataVenda) = YEAR(CURRENT_DATE) GROUP BY MONTH(f.dataVenda) ORDER BY MONTH(f.dataVenda)")
    List<MesValor> getValoresLucroMensalFromDataBase();
}
