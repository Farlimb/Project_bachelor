package com.example.bakalarka_jpa.services;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.bakalarka_jpa.dto.CreateRequestDTO;
import com.example.bakalarka_jpa.dto.FindRequestDTO;
import com.example.bakalarka_jpa.dto.FindResponseDTO;
import com.example.bakalarka_jpa.dto.UpdateRequestDTO;
import com.example.bakalarka_jpa.entities.PohladavkaEntity;
import com.example.bakalarka_jpa.interfaces.PohladavkaJPA;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.language.ColognePhonetic;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.springframework.http.HttpStatus.*;

@Service
public class PohladavkaService {
    private final PohladavkaJPA pohladavkaJPA;
    private ColognePhonetic colner = new ColognePhonetic();
    public PohladavkaService(PohladavkaJPA pohladavkaJPA) {
        this.pohladavkaJPA = pohladavkaJPA;
    }
    private int meno_zhoda = 0;
    private int priezvisko_zhoda = 0;
    private int obec_zhoda = 0;
    private int ulica_zhoda = 0;
    private int celkova_zhoda = 0;
    private String nanoId;

    public String DeleteByParams(FindRequestDTO list) {         //funkcia na vymazanie záznamu dlžníka

        PohladavkaEntity toDeleteRecord = pohladavkaJPA.findFirstByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolnerAndObecAndUlicaAndNanoId(colner.encode(normalizeName(list.meno())), colner.encode(normalizeName(list.priezvisko())),list.obec(),list.ulica(), list.nanoId());
        //Vyhľadanie konkrétneho záznamu aj s jeho ID v databáze

        if(toDeleteRecord == null){ //Vrátenie chybovej hlášky ak záznam nebol nájdený
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        }
        pohladavkaJPA.deleteById(toDeleteRecord.getId());  //vymazanie záznamu na základe ID v databáze
        throw new ResponseStatusException(ACCEPTED, "Successfully deleted");
    }


    public FindResponseDTO CreateByParams(CreateRequestDTO list){   // funkcia na vytvorenie nového záznamu dlžníka

        if((isBlank(list.meno())) || isBlank(list.priezvisko()) || isBlank(list.obec()) || isBlank(list.ulica())){    //Kontrola či nevyrábame neúplný záznam
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        }
        if(list.meno().isEmpty() || list.priezvisko().isEmpty() || list.obec().isEmpty() || list.ulica().isEmpty()){ //Kontrola či nevyrábame neúplný záznam
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        }

        //vytváranie nového záznamu s aplikáciou úprav vstupu
        PohladavkaEntity newRecord = new PohladavkaEntity();
        newRecord.setNanoId(NanoIdUtils.randomNanoId());
        newRecord.setPrve_meno(capitalize(list.meno()));
        newRecord.setPrveMenoUpravene(normalizeName(list.meno()));
        newRecord.setPrveMenoUpraveneKolner(colner.encode(normalizeName(list.meno())));

        newRecord.setPriezvisko(capitalize(list.priezvisko()));
        newRecord.setPriezviskoUpravene(normalizeName(list.priezvisko()));
        newRecord.setPriezviskoUpraveneKolner(colner.encode(normalizeName(list.priezvisko())));

        newRecord.setObec(list.obec());
        newRecord.setUlica(list.ulica());

        pohladavkaJPA.saveAndFlush(newRecord);
        return new FindResponseDTO(newRecord.getPrve_meno(), newRecord.getPriezvisko(), newRecord.getUlica(), newRecord.getObec(), 100, newRecord.getNanoId());
    }

    public FindResponseDTO FindBestByParams(FindRequestDTO list) { //funkcia na vyhľadanie záznamu alebo záznamov sediacich pre vstupné údaje
        //deklarácia premien pre ďalšiu prácu s nimi
        String meno_upravene = normalizeName(list.meno());
        String priezvisko_upravene = normalizeName(list.priezvisko());
        String meno_Kolner = colner.encode(meno_upravene);
        String priezvisko_Kolner = colner.encode(priezvisko_upravene);

        if(meno_Kolner==null){ //musí sa zedifinovať ináč neprejde funckia findallby
            meno_Kolner = "x";
        }

        if(priezvisko_Kolner==null){ //musí sa zedifinovať ináč neprejde funckia findallby
            priezvisko_Kolner = "x";
        }

        //Vyhľadanie všetkých záznamov s rovnakou fonetickou stopou prvého mena a priezviska
        List<PohladavkaEntity> result = pohladavkaJPA.findAllByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolnerOrNanoId(meno_Kolner, priezvisko_Kolner, list.nanoId());

        //Set<FindResponseDTO> setEntit = new HashSet<>();
        List<FindResponseDTO> sortedList =  new ArrayList<>();;
        if(result!=null) {
            for (PohladavkaEntity entity : result) {
                celkova_zhoda = calculateMatch(list, entity, celkova_zhoda);
                sortedList.add(new FindResponseDTO(entity.getPrve_meno(), entity.getPriezvisko(), entity.getUlica(), entity.getObec(), celkova_zhoda, entity.getNanoId()));
            }
        }

        // sort setEntit in descending order based on celkova_zhoda
        Collections.sort(sortedList, new Comparator<FindResponseDTO>() {
            @Override
            public int compare(FindResponseDTO o1, FindResponseDTO o2) {
                return Double.compare(o2.match(), o1.match());
            }
        });

        if (!sortedList.isEmpty()) {
            if(sortedList.get(0).match()>=80) {
                return sortedList.get(0); // Vracia prvý záznam s najvyššou zhodou, ak je nad 80%
            }
            else{
                return null;
            }
        } else {
            return null; // Vracia null ak nie sú splnené podmienky
        }
    }


    public List<FindResponseDTO> FindByParams(FindRequestDTO list) { //funkcia na vyhľadanie záznamu alebo záznamov sediacich pre vstupné údaje
        //deklarácia premien pre ďalšiu prácu s nimi

        String meno_upravene = normalizeName(list.meno());
        String priezvisko_upravene = normalizeName(list.priezvisko());
        String meno_Kolner = colner.encode(meno_upravene);
        String priezvisko_Kolner = colner.encode(priezvisko_upravene);

        if(meno_Kolner==null){ //musí sa zedifinovať ináč neprejde funckia findallby
            meno_Kolner = "x";
        }

        if(priezvisko_Kolner==null){ //musí sa zedifinovať ináč neprejde funckia findallby
            priezvisko_Kolner = "x";
        }

        //Vyhľadanie všetkých záznamov s rovnakou fonetickou stopou prvého mena a priezviska
        List<PohladavkaEntity> result = pohladavkaJPA.findAllByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolnerOrNanoId(meno_Kolner, priezvisko_Kolner, list.nanoId());

        //Set<FindResponseDTO> setEntit = new HashSet<>();
        List<FindResponseDTO> sortedList =  new ArrayList<>();;
        if(result!=null) {
            for (PohladavkaEntity entity : result) {
                celkova_zhoda = calculateMatch(list, entity, celkova_zhoda);
                if (celkova_zhoda > 30) {
                    sortedList.add(new FindResponseDTO(entity.getPrve_meno(), entity.getPriezvisko(), entity.getUlica(), entity.getObec(), celkova_zhoda, entity.getNanoId()));
                }
            }
        }

        // sort setEntit in descending order based on celkova_zhoda
        Collections.sort(sortedList, new Comparator<FindResponseDTO>() {
            @Override
            public int compare(FindResponseDTO o1, FindResponseDTO o2) {
                return Double.compare(o2.match(), o1.match());
            }
        });

        if (!sortedList.isEmpty()) {
            return sortedList; // return the first (highest match) element of the sorted list
        } else {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource"); // or return null if the list is empty
        }
    }


    public FindResponseDTO UpdateByParams(UpdateRequestDTO list) { //funkcia na zaevidovanie zmeny
        String meno_Kolner = colner.encode(normalizeName(list.meno()));
        String priezvisko_Kolner = colner.encode(normalizeName(list.priezvisko()));
        nanoId = list.nanoId();
        PohladavkaEntity result = pohladavkaJPA.findFirstByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolnerAndObecAndUlicaAndNanoId(meno_Kolner, priezvisko_Kolner,list.obec(),list.ulica(), nanoId);
        PohladavkaEntity checkUp = pohladavkaJPA.findFirstByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolnerAndObecAndUlicaAndNanoId(colner.encode(normalizeName(list.menoUprava())), colner.encode(normalizeName(list.priezviskoUprava())),list.obecUprava(),list.ulicaUprava(), nanoId);
        System.out.println(result);
        System.out.println(checkUp);
        if(checkUp != null){ //kontrola či nové dáta sa úplne nezhodujú so starými dátami
            throw new ResponseStatusException(NOT_ACCEPTABLE, "Record of person already exists!");
        }

        if(result == null){ //kontrola či staré dáta existujú
            throw new ResponseStatusException(NOT_ACCEPTABLE, "Unable to find resource");
        }

        //Vytváranie nového záznamu s aplikáciou úprav vstupu
        PohladavkaEntity newUpdatedRecord = new PohladavkaEntity();

        newUpdatedRecord.setNanoId(list.nanoId()); //Zaevidovanie toho istého nanoId keďže ide o tú istú osobu, zabezpečuje evidenciu dlžníka s meniacimi sa údajmi

        if(!list.menoUprava().isEmpty()) {
            newUpdatedRecord.setPrve_meno(list.menoUprava());
            newUpdatedRecord.setPrveMenoUpravene(normalizeName(list.menoUprava()));
            newUpdatedRecord.setPrveMenoUpraveneKolner(colner.encode(normalizeName(list.menoUprava())));
        }
        else{
            newUpdatedRecord.setPrve_meno(list.meno());
            newUpdatedRecord.setPrveMenoUpravene(normalizeName(list.meno()));
            newUpdatedRecord.setPrveMenoUpraveneKolner(colner.encode(normalizeName(list.meno())));
        }

        if(!list.priezviskoUprava().isEmpty()) {
            newUpdatedRecord.setPriezvisko(list.priezviskoUprava());
            newUpdatedRecord.setPriezviskoUpravene(normalizeName(list.priezviskoUprava()));
            newUpdatedRecord.setPriezviskoUpraveneKolner(colner.encode(normalizeName(list.priezviskoUprava())));
        }
        else{
            newUpdatedRecord.setPriezvisko(list.priezvisko());
            newUpdatedRecord.setPriezviskoUpravene(normalizeName(list.priezvisko()));
            newUpdatedRecord.setPriezviskoUpraveneKolner(colner.encode(normalizeName(list.priezvisko())));
        }

        if(!list.obecUprava().isEmpty()){
            newUpdatedRecord.setObec(list.obecUprava());
        }
        else{
            newUpdatedRecord.setObec(list.obec());
        }

        if(!list.ulicaUprava().isEmpty()){
            newUpdatedRecord.setUlica(list.ulicaUprava());
        }
        else{
            newUpdatedRecord.setUlica(list.ulica());
        }

//        System.out.println(newUpdatedRecord.getPrve_meno());
//        System.out.println(newUpdatedRecord.getPriezvisko());
//        System.out.println(newUpdatedRecord.getObec());
//        System.out.println(newUpdatedRecord.getUlica());
        PohladavkaEntity checkTwo = pohladavkaJPA.findFirstByPrveMenoUpraveneKolnerAndPriezviskoUpraveneKolnerAndObecAndUlicaAndNanoId(colner.encode(normalizeName(newUpdatedRecord.getPrve_meno())), colner.encode(normalizeName(newUpdatedRecord.getPriezvisko())),newUpdatedRecord.getObec(),newUpdatedRecord.getUlica(), nanoId);
        System.out.println(checkTwo);
        if(checkTwo!=null) {
            if (Objects.equals(checkTwo.getPrve_meno(), newUpdatedRecord.getPrve_meno()) && Objects.equals(checkTwo.getPriezvisko(), newUpdatedRecord.getPriezvisko())
                    && Objects.equals(checkTwo.getObec(), newUpdatedRecord.getObec()) && Objects.equals(checkTwo.getUlica(), newUpdatedRecord.getUlica())) { //kontrola či nové dáta sa úplne nezhodujú so starými dátami
                throw new ResponseStatusException(NOT_ACCEPTABLE, "Unable to find resource");
            }
        }
        pohladavkaJPA.saveAndFlush(newUpdatedRecord);
        return new FindResponseDTO(newUpdatedRecord.getPrve_meno(),newUpdatedRecord.getPriezvisko(),newUpdatedRecord.getUlica(),newUpdatedRecord.getObec(),100,newUpdatedRecord.getNanoId());
    }



    public void ConvertAll() {  //funckia na upravenie údajov a uloženie do príšlušných stĺpcov v databáze
        ArrayList<PohladavkaEntity> list = new ArrayList<>();
        pohladavkaJPA.findAll().forEach(pohladavkaEntity -> {
            pohladavkaEntity.setPriezviskoUpravene(normalizeName(pohladavkaEntity.getPriezvisko()));
            pohladavkaEntity.setPrveMenoUpravene(normalizeName(pohladavkaEntity.getPrve_meno()));
            pohladavkaEntity.setPriezviskoUpraveneKolner(colner.encode(pohladavkaEntity.getPriezviskoUpravene()));
            pohladavkaEntity.setPrveMenoUpraveneKolner(colner.encode(pohladavkaEntity.getPrveMenoUpravene()));
            pohladavkaEntity.setNanoId(NanoIdUtils.randomNanoId());
            list.add(pohladavkaEntity);
        });
        pohladavkaJPA.saveAll(list);
    }

    public String normalizeName(String zaznam) { //funkcia na úpravu mien a priezvisk pre fonetický algoritmus
        if (zaznam != null) {
            zaznam = capitalize(zaznam);
            zaznam = zaznam.replace("{", "");
            zaznam = zaznam.replace("}", "");
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
            zaznam = removeDuplicates(zaznam);
            zaznam = zaznam.trim();
        }
        return zaznam;
    }
    public static String capitalize(String str)
    {
        if (str == null || str.length() == 0) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public String removeDuplicates(String string) { //funkcia na odstránenie písmen v mene a priezvisku ktoré sa opakujú
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

    public int calculateMatch(FindRequestDTO list, PohladavkaEntity entity, int celkova_zhoda) {

        if (list.meno() != null && entity.getPrve_meno() !=null) {
            meno_zhoda = FuzzySearch.ratio(capitalize(list.meno()), entity.getPrve_meno());
            System.out.println(meno_zhoda);
        }

        if (list.priezvisko() != null && entity.getPriezvisko() !=null) {
            priezvisko_zhoda = FuzzySearch.ratio(capitalize(list.priezvisko()), entity.getPriezvisko());
            System.out.println(priezvisko_zhoda);
        }

        if (list.obec() != null && !list.obec().equals("") && entity.getObec() !=null && !entity.getObec().equals("")) {
            obec_zhoda = FuzzySearch.weightedRatio(list.obec(), entity.getObec());
            System.out.println(obec_zhoda);
        }
        if (list.ulica() != null && !list.ulica().equals("") && entity.getUlica() !=null && !entity.getUlica().equals("")) {
            ulica_zhoda = FuzzySearch.weightedRatio(list.ulica(), entity.getUlica());
            System.out.println(ulica_zhoda);
        }
        celkova_zhoda = Math.min(meno_zhoda, priezvisko_zhoda);

        if (list.obec() != null && !list.obec().equals("") && entity.getObec() !=null && !entity.getObec().equals("")) {
            System.out.println(list.obec());
            celkova_zhoda = Math.min(celkova_zhoda, obec_zhoda);
        }

        if (list.ulica() != null && !list.ulica().equals("") && entity.getUlica() !=null && !entity.getUlica().equals("")){
            System.out.println(list.ulica());
            celkova_zhoda = Math.min(celkova_zhoda, ulica_zhoda);
        }
        if (Objects.equals(list.nanoId(), entity.getNanoId())) {
            System.out.println(list.nanoId());
            celkova_zhoda = 100;
        }
        return celkova_zhoda;
    }

    private boolean isBlank(String string){
        return string==null || string.trim().isEmpty();
    }
}
