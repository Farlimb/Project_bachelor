package com.example.bakalarka_jpa.interfaces;

import com.example.bakalarka_jpa.entities.PohladavkaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PohladavkaJPA extends JpaRepository<PohladavkaEntity,Integer>{
    List<PohladavkaEntity> findAllByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolner(String meno, String priezvisko);
    List<PohladavkaEntity> findAllByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolnerAndObecAndUlicaOrNanoId(String meno, String priezvisko, String obec, String ulica, String nanoId);
}
