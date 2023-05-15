package com.example.id_system.interfaces;

import com.example.id_system.entities.PohladavkaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PohladavkaJPA extends JpaRepository<PohladavkaEntity,Integer>{
    List<PohladavkaEntity> findAllByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolner(String meno, String priezvisko);
    List<PohladavkaEntity> findAllByNanoId(String nanoId);
    List<PohladavkaEntity> findAllByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolnerOrNanoId(String meno, String priezvisko, String nanoId);
    //PohladavkaEntity findFirstByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolnerAndObecAndUlica(String meno, String priezvisko, String obec, String ulica);
    PohladavkaEntity findFirstByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolnerAndObecAndUlicaAndNanoId(String meno, String priezvisko, String obec, String ulica, String nanoId);
}
