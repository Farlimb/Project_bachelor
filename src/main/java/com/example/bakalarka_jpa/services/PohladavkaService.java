package com.example.bakalarka_jpa.services;

import com.example.bakalarka_jpa.entities.PohladavkaEntity;
import com.example.bakalarka_jpa.interfaces.PohladavkaJPA;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.language.ColognePhonetic;

import java.util.Optional;

@Service
public class PohladavkaService {
    private final PohladavkaJPA pohladavkaJPA;

    public PohladavkaService(PohladavkaJPA pohladavkaJPA) {
        this.pohladavkaJPA = pohladavkaJPA;
    }

    public Optional<PohladavkaEntity> FindById(int Id){
        return pohladavkaJPA.findById(Id);
    }
    public void ConvertAll(){
        ColognePhonetic colner = new ColognePhonetic();
        pohladavkaJPA.findAll().forEach(pohladavkaEntity -> {
        pohladavkaEntity.setPriezviskoUpraveneKolner(colner.encode(pohladavkaEntity.getPriezviskoUpravene()));
        pohladavkaEntity.setPrveMenoUpraveneKolner(colner.encode(pohladavkaEntity.getPrveMenoUpravene()));
        pohladavkaJPA.save(pohladavkaEntity);
        });
    }
}
