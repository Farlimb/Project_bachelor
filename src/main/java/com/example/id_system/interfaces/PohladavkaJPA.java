package com.example.id_system.interfaces;

import com.example.id_system.entities.PohladavkaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Rozhranie PohladavkaJPA poskytuje databázové operácie pre PohladavkaEntity pomocou JPA.
 * Rozširuje rozhranie JpaRepository.
 */
@Repository
public interface PohladavkaJPA extends JpaRepository<PohladavkaEntity,Integer>{
    //List<PohladavkaEntity> findAllByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolner(String meno, String priezvisko);
    //List<PohladavkaEntity> findAllByNanoId(String nanoId);
    /**
     * Získa z databázy zoznam objektov PohladavkaEntity, ktoré zodpovedajú zadanému krstnému menu,
     * priezvisko alebo Nano ID.
     *
     * @param meno Krstné meno, ktoré sa má vyhľadať.
     * @param priezvisko Priezvisko, ktoré sa má vyhľadať.
     * @param nanoId Identifikátor Nano, ktorý sa má vyhľadať.
     * @return Zoznam objektov PohladavkaEntity zodpovedajúcich kritériám vyhľadávania.
     */
    List<PohladavkaEntity> findAllByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolnerOrNanoId(String meno, String priezvisko, String nanoId);
    //PohladavkaEntity findFirstByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolnerAndObecAndUlica(String meno, String priezvisko, String obec, String ulica);
    /**
     * Vyhľadá prvý objekt PohladavkaEntity z databázy, ktorý zodpovedá zadanému krstnému menu,
     * priezvisko, lokalitu, ulicu a Nano ID.
     *
     * @param meno Krstné meno, ktoré sa má vyhľadať.
     * @param priezvisko Priezvisko, ktoré sa má vyhľadať.
     * @param obec Lokalita, v ktorej sa má vyhľadávať.
     * @param ulica Ulica, ktorá sa má vyhľadať.
     * @param nanoId Identifikačné číslo nano, ktoré sa má vyhľadať.
     * @return Prvý objekt PohladavkaEntity, ktorý vyhovuje kritériám vyhľadávania, alebo null, ak nebol nájdený žiadny.
     */
    PohladavkaEntity findFirstByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolnerAndObecAndUlicaAndNanoId(String meno, String priezvisko, String obec, String ulica, String nanoId);
}
