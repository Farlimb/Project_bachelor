package com.example.bakalarka_jpa.interfaces;

import com.example.bakalarka_jpa.entities.PohladavkaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PohladavkaJPA extends JpaRepository<PohladavkaEntity,Integer>{
    //List<PohladavkaEntity> findPohladavkaEntityBy(String prve_meno);

}
