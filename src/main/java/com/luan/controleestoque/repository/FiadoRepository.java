package com.luan.controleestoque.repository;

import com.luan.controleestoque.model.Fiado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FiadoRepository extends JpaRepository<Fiado, Long> {
}
