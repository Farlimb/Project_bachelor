package com.example.bakalarka_jpa.services;

import com.example.bakalarka_jpa.dto.FindRequestDTO;
import com.example.bakalarka_jpa.dto.FindResponseDTO;
import com.example.bakalarka_jpa.dto.UpdateRequestDTO;
import com.example.bakalarka_jpa.entities.PohladavkaEntity;
import com.example.bakalarka_jpa.interfaces.PohladavkaJPA;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.language.ColognePhonetic;
import java.io.*;
import java.util.*;

@Service
public class PohladavkaService {
    private final PohladavkaJPA pohladavkaJPA;
    ColognePhonetic colner = new ColognePhonetic();

    public PohladavkaService(PohladavkaJPA pohladavkaJPA) {
        this.pohladavkaJPA = pohladavkaJPA;
    }

    public Optional<PohladavkaEntity> FindById(int Id) {
        return pohladavkaJPA.findById(Id);
    }

    public Set<FindResponseDTO> FindByParams(FindRequestDTO list) {
        String meno_upravene = normalizeName(list.meno());
        String priezvisko_upravene = normalizeName(list.priezvisko());
        String meno_Kolner = colner.encode(meno_upravene);
        String priezvisko_Kolner = colner.encode(priezvisko_upravene);
        List<PohladavkaEntity> result = pohladavkaJPA.findAllByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolner(meno_Kolner, priezvisko_Kolner);
        Set<FindResponseDTO> setEntit = new HashSet<>();
        for (PohladavkaEntity entity : result) {
            setEntit.add(new FindResponseDTO(entity.getPrveMenoUpravene(), entity.getPriezviskoUpravene(), entity.getUlica(), entity.getObec()));
        }
        return setEntit;
    }

    public String UpdateByParams(UpdateRequestDTO list) {
        String meno_Kolner = colner.encode(normalizeName(list.meno()));
        String priezvisko_Kolner = colner.encode(normalizeName(list.priezvisko()));
        String nanoId = list.nanoId();

        if(nanoId == null) {
            nanoId = "null";
        }
        List<PohladavkaEntity> result = pohladavkaJPA.findAllByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolnerAndObecAndUlicaOrNanoId(meno_Kolner, priezvisko_Kolner,list.obec(),list.ulica(), nanoId);
        if(result.isEmpty()){
            return "Not updated, couldn't find record";
        }
        for (PohladavkaEntity entity : result) {
            if(list.priezviskoUprava() != null)
                entity.setPriezvisko(normalizeName(list.priezviskoUprava()));
                entity.setPriezviskoUpraveneKolner(colner.encode(normalizeName(list.priezviskoUprava())));
            if(list.menoUprava() != null)
                entity.setPrve_meno(normalizeName(list.menoUprava()));
                entity.setPrveMenoUpraveneKolner(colner.encode(normalizeName(list.menoUprava())));
            if(list.obecUprava() != null)
                entity.setObec(list.obecUprava());
            if(list.ulicaUprava() != null)
                entity.setUlica(list.ulicaUprava());

        }
        pohladavkaJPA.saveAll(result);

        return "Sucessfuly updated " +  result.size();
    }

    public Set<FindResponseDTO> FindorCreateByParams(FindRequestDTO list) {
        String  meno_Kolner = colner.encode(normalizeName(list.meno()));
        String priezvisko_Kolner = colner.encode(normalizeName(list.priezvisko()));
        List<PohladavkaEntity> result = pohladavkaJPA.findAllByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolner(meno_Kolner, priezvisko_Kolner);

        Set<FindResponseDTO> setEntit = new HashSet<>();
        for (PohladavkaEntity entity : result) {
            setEntit.add(new FindResponseDTO(entity.getPrveMenoUpravene(), entity.getPriezviskoUpravene(), entity.getUlica(), entity.getObec()));
        }

        return setEntit;
    }

    public void ConvertAll() {
        ArrayList<PohladavkaEntity> list = new ArrayList<>();
        pohladavkaJPA.findAll().forEach(pohladavkaEntity -> {
            pohladavkaEntity.setPriezviskoUpravene(normalizeName(pohladavkaEntity.getPriezvisko()));
            pohladavkaEntity.setPrveMenoUpravene(normalizeName(pohladavkaEntity.getPrve_meno()));
            pohladavkaEntity.setPriezviskoUpraveneKolner(colner.encode(pohladavkaEntity.getPriezviskoUpravene()));
            pohladavkaEntity.setPrveMenoUpraveneKolner(colner.encode(pohladavkaEntity.getPrveMenoUpravene()));
            list.add(pohladavkaEntity);
        });
        pohladavkaJPA.saveAll(list);
    }

    public String normalizeName(String zaznam) {
        if (zaznam != null) {
            zaznam = zaznam.replace("{", "");
            zaznam = zaznam.replace("}", "");
            zaznam = zaznam.replace("Sz", "S");
            zaznam = zaznam.replace("sz", "s");
            zaznam = zaznam.replace("gy", "d");
            zaznam = zaznam.replace("Gy", "D");
            zaznam = zaznam.replace("Dzs", "Dz");
            zaznam = zaznam.replace("Zs", "Z");
            zaznam = zaznam.replace("zs", "z");
            zaznam = zaznam.replace("Cs", "C");
            zaznam = zaznam.replace("cs", "c");
            zaznam = zaznam.replace("cz", "c");
            zaznam = zaznam.replace("Cz", "C");
            zaznam = zaznam.replace("Ď", "D");
            zaznam = zaznam.replace("ď", "d");
            zaznam = zaznam.replace("Č", "C");
            zaznam = zaznam.replace('č', 'c');
            zaznam = zaznam.replace('ľ', 'l');
            zaznam = zaznam.replace('Ľ', 'L');
            zaznam = zaznam.replace('ĺ', 'l');
            zaznam = zaznam.replace('Ĺ', 'L');
            zaznam = zaznam.replace('ň', 'n');
            zaznam = zaznam.replace('Ň', 'N');
            zaznam = zaznam.replace('Š', 'S');
            zaznam = zaznam.replace('š', 's');
            zaznam = zaznam.replace('Ť', 'T');
            zaznam = zaznam.replace('ť', 't');
            zaznam = zaznam.replace('Ž', 'Z');
            zaznam = zaznam.replace('ž', 'z');
            zaznam = zaznam.replace("dž", "dz");
            zaznam = zaznam.replace("Dž", "Dz");
            zaznam = zaznam.replace('Ŕ', 'R');
            zaznam = zaznam.replace('ŕ', 'r');
            zaznam = removeDuplicates(zaznam);
        }
        return zaznam;
    }


    public String removeDuplicates(String string) {
        if (string == null) {
            return null;
        }
        char[] chars = string.toCharArray();
        char previous = ' ';
        int i = 0;
        for (char c : chars) {
            if (c != previous) {
                chars[i++] = c;
                previous = c;
            }
        }
        return new String(chars).substring(0, i);

    }
}
