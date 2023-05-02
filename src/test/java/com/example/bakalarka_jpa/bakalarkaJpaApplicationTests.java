package com.example.bakalarka_jpa;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.bakalarka_jpa.dto.FindRequestDTO;
import com.example.bakalarka_jpa.dto.FindResponseDTO;
import com.example.bakalarka_jpa.entities.PohladavkaEntity;
import com.example.bakalarka_jpa.services.PohladavkaService;
import org.apache.commons.codec.language.ColognePhonetic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class bakalarkaJpaApplicationTests {
    @Autowired
    private PohladavkaService pohladavkaService;
    private PohladavkaEntity entity = new PohladavkaEntity();
    private final ColognePhonetic colner = new ColognePhonetic();
    private int celkova_zhoda=0;
    @Test
    void normalizePriezviskoTestTrue() {
        assert pohladavkaService.normalizeName("").equals("");
        assert pohladavkaService.normalizeName("Gyori").equals("Dori");
        assert pohladavkaService.normalizeName("Andrássy").equals("Andrásy");
        assert pohladavkaService.normalizeName("Magyar").equals("Madar");
        assert pohladavkaService.normalizeName("Szabó").equals("Sabó");
        assert pohladavkaService.normalizeName("Puškáš").equals("Puskás");
        assert pohladavkaService.normalizeName("Zsiga").equals("Ziga");
        assert pohladavkaService.normalizeName("Huszár").equals("Husár");
        assert pohladavkaService.normalizeName("Droppa").equals("Dropa");
        assert pohladavkaService.normalizeName("Ďurča").equals("Durca");
        assert pohladavkaService.normalizeName("Zsigmondy").equals("Zigmondy");
        assert pohladavkaService.normalizeName("Országh").equals("Orságh");
    }
    @Test
    void normalizePriezviskoTestFalse() {
        assert !pohladavkaService.normalizeName("Horváth").equals("Horvát");
        assert !pohladavkaService.normalizeName("Kovács").equals("Kováč");
        assert !pohladavkaService.normalizeName("Magyar").equals("Madiar");
        assert !pohladavkaService.normalizeName("Tóth").equals("Tót");
        assert !pohladavkaService.normalizeName("Nagy").equals("Nady");
        assert !pohladavkaService.normalizeName("Baloghová").equals("Balogová");
        assert !pohladavkaService.normalizeName("Lukácsová").equals("Lukáčová");
        assert !pohladavkaService.normalizeName("Szabó").equals("Szabó");
        assert !pohladavkaService.normalizeName("Keresteš").equals("Keserteš");
        assert !pohladavkaService.normalizeName("Némethová").equals("Németová");
        assert !pohladavkaService.normalizeName("Kollár").equals("Kolar");
    }

    @Test
    void phoneticTest() {
        assert colner.encode("Philip").equals(colner.encode("Filip"));
        assert colner.encode("Tóth").equals(colner.encode("Tot"));
        assert colner.encode("Balogová").equals(colner.encode("Baloghová"));
        assert colner.encode("Gimeszi").equals(colner.encode("Gymeszy"));
        assert colner.encode("Palaj").equals(colner.encode("Palai"));
        assert colner.encode("Niezgodský").equals(colner.encode("Hniezgodsky"));
        assert colner.encode("Petrík").equals(colner.encode("Pedrik"));
        assert colner.encode("Vták").equals(colner.encode("Fták"));
        assert colner.encode("Mozog").equals(colner.encode("Mozok"));
        assert colner.encode("Pez").equals(colner.encode("Pes"));
        assert colner.encode("Atila").equals(colner.encode("Atylla"));
    }

    @Test
    void normalizeAndPhoneticTest() {
        assert colner.encode(pohladavkaService.normalizeName("")).equals(colner.encode(pohladavkaService.normalizeName("")));
        assert colner.encode(pohladavkaService.normalizeName("Horváth")).equals(colner.encode(pohladavkaService.normalizeName("Horvát")));
        assert colner.encode(pohladavkaService.normalizeName("Kovács")).equals(colner.encode(pohladavkaService.normalizeName("Kováč")));
        assert colner.encode(pohladavkaService.normalizeName("Ďuri")).equals(colner.encode(pohladavkaService.normalizeName("Gyori")));
        assert colner.encode(pohladavkaService.normalizeName("Lukácsová")).equals(colner.encode(pohladavkaService.normalizeName("Lukáčová")));
        assert colner.encode(pohladavkaService.normalizeName("Némethová")).equals(colner.encode(pohladavkaService.normalizeName("Németová")));
        assert colner.encode(pohladavkaService.normalizeName("Cséfalvay")).equals(colner.encode(pohladavkaService.normalizeName("Čéfalvai")));
        assert colner.encode(pohladavkaService.normalizeName("Csaba")).equals(colner.encode(pohladavkaService.normalizeName("Čaba")));
        assert colner.encode(pohladavkaService.normalizeName("Zsuzsanna")).equals(colner.encode(pohladavkaService.normalizeName("Žužanna")));
        assert colner.encode(pohladavkaService.normalizeName("Nyúlová")).equals(colner.encode(pohladavkaService.normalizeName("Ňúlová")));
        assert colner.encode(pohladavkaService.normalizeName("Bungyi")).equals(colner.encode(pohladavkaService.normalizeName("Bundi")));
        assert colner.encode(pohladavkaService.normalizeName("Kosztolányi")).equals(colner.encode(pohladavkaService.normalizeName("Kostoláni")));
    }

    @Test
    void calculateMatchTest() {

        entity.setPrve_meno("");
        entity.setPriezvisko("");
        entity.setUlica("");
        entity.setObec("");
        entity.setNanoId(NanoIdUtils.randomNanoId());
        FindRequestDTO FindRequestDTO = new FindRequestDTO("Filip", "Kušnír", "Gomenzkého 41", "Košice", NanoIdUtils.randomNanoId());
        celkova_zhoda = pohladavkaService.calculateMatch(FindRequestDTO,entity, celkova_zhoda);
        System.out.println("Celkova zhoda = "  + celkova_zhoda);
        assert celkova_zhoda == 0;

        entity.setPrve_meno("Filip");
        entity.setPriezvisko("Kušnír");
        entity.setUlica("Komenského 41");
        entity.setObec("Košice");
        entity.setNanoId(NanoIdUtils.randomNanoId());
        FindRequestDTO = new FindRequestDTO("", "", "", "", NanoIdUtils.randomNanoId());
        celkova_zhoda = pohladavkaService.calculateMatch(FindRequestDTO,entity, celkova_zhoda);
        System.out.println("Celkova zhoda = "  + celkova_zhoda);
        assert celkova_zhoda == 0;

        entity.setPrve_meno("");
        entity.setPriezvisko("Kušnír");
        entity.setUlica("Komenského 41");
        entity.setObec("Košice");
        entity.setNanoId(NanoIdUtils.randomNanoId());
        FindRequestDTO = new FindRequestDTO("Filip", "Kušnír", "Gomenzkého 41", "Košice", NanoIdUtils.randomNanoId());
        celkova_zhoda = pohladavkaService.calculateMatch(FindRequestDTO,entity, celkova_zhoda);
        System.out.println("Celkova zhoda = "  + celkova_zhoda);
        assert celkova_zhoda == 0;

        entity.setPrve_meno("Filip");
        entity.setPriezvisko("Kušnír");
        entity.setUlica("Komenského 41");
        entity.setObec("Košice");
        entity.setNanoId(NanoIdUtils.randomNanoId());
        FindRequestDTO = new FindRequestDTO("Filip", "Kušnír", "Gomenzkého 41", "Košice", NanoIdUtils.randomNanoId());
        celkova_zhoda = pohladavkaService.calculateMatch(FindRequestDTO,entity, celkova_zhoda);
        System.out.println("Celkova zhoda = "  + celkova_zhoda);
        assert celkova_zhoda > 80;

        entity.setPrve_meno("Filip");
        entity.setPriezvisko("Kušnír");
        entity.setUlica("Komenského 41");
        entity.setObec("Košice");
        entity.setNanoId(NanoIdUtils.randomNanoId());
        FindRequestDTO = new FindRequestDTO("Bruno", "Sabadoš", "Komenského 40", "Košice", NanoIdUtils.randomNanoId());
        celkova_zhoda = pohladavkaService.calculateMatch(FindRequestDTO,entity, celkova_zhoda);
        System.out.println("celkova zhoda = " + celkova_zhoda);
        assert celkova_zhoda == 0;

        entity.setPrve_meno("Filip");               //test na zmenené osobné údaje ale identifikovaný ako ten istý človek cez nanoId
        entity.setPriezvisko("Kušnír");
        entity.setUlica("Komenského 41");
        entity.setObec("Košice");
        entity.setNanoId(NanoIdUtils.randomNanoId());
        FindRequestDTO = new FindRequestDTO("Bruno", "Sabadoš", "Tomášikova 30", "Bratislava", entity.getNanoId());
        celkova_zhoda = pohladavkaService.calculateMatch(FindRequestDTO,entity, celkova_zhoda);
        System.out.println("celkova zhoda = " + celkova_zhoda);
        assert celkova_zhoda == 100;

        entity.setPrve_meno("Filip");
        entity.setPriezvisko("Kušnír");
        entity.setUlica("Komenského 41");
        entity.setObec("Košice");
        entity.setNanoId(NanoIdUtils.randomNanoId());
        FindRequestDTO = new FindRequestDTO("Filip", "Kušnír", "Komenského 41", "Bratislava", NanoIdUtils.randomNanoId());
        celkova_zhoda =pohladavkaService.calculateMatch(FindRequestDTO,entity, celkova_zhoda);
        System.out.println("celkova zhoda = " + celkova_zhoda);
        assert celkova_zhoda > 0;

        entity.setPrve_meno("Filip");
        entity.setPriezvisko("Kušnír");
        entity.setUlica("Komenského 41");
        entity.setObec("Košice");
        entity.setNanoId(NanoIdUtils.randomNanoId());
            FindRequestDTO = new FindRequestDTO("Filip", "Kušnír", "Kotbutská 41", "Košice", NanoIdUtils.randomNanoId());
        celkova_zhoda = pohladavkaService.calculateMatch(FindRequestDTO,entity, celkova_zhoda);
        System.out.println("celkova zhoda = " + celkova_zhoda);
        assert celkova_zhoda < 80;

        entity.setPrve_meno("Atilla");
        entity.setPriezvisko("Grónsky");
        entity.setUlica("Komenského 41");
        entity.setObec("Košice");
        entity.setNanoId(NanoIdUtils.randomNanoId());
        FindRequestDTO = new FindRequestDTO("Atila", "Grúnsky", "Komenského 41", "Košice", NanoIdUtils.randomNanoId());
        celkova_zhoda = pohladavkaService.calculateMatch(FindRequestDTO,entity, celkova_zhoda);
        System.out.println("celkova zhoda = " +celkova_zhoda);
        assert celkova_zhoda > 80;

        entity.setPrve_meno("Tomáš");
        entity.setPriezvisko("Kováč");
        entity.setUlica("Madridská 22");
        entity.setObec("Košice");
        entity.setNanoId(NanoIdUtils.randomNanoId());
        FindRequestDTO = new FindRequestDTO("Tomáš", "Kováč", "Madridská", "Košice", NanoIdUtils.randomNanoId());
        celkova_zhoda = pohladavkaService.calculateMatch(FindRequestDTO,entity, celkova_zhoda);
        System.out.println("celkova zhoda = " +celkova_zhoda);
        assert celkova_zhoda > 80;

        entity.setPrve_meno("Filip");
        entity.setPriezvisko("Kovács");
        entity.setUlica("Nám. Osloboditeľov 13");
        entity.setObec("Košice");
        entity.setNanoId(NanoIdUtils.randomNanoId());
        FindRequestDTO = new FindRequestDTO("Filip", "Kovács", "Námestie Osloboditeľov 13", "Košice", NanoIdUtils.randomNanoId());
        celkova_zhoda = pohladavkaService.calculateMatch(FindRequestDTO,entity, celkova_zhoda);
        System.out.println("celkova zhoda = " +celkova_zhoda);
        assert celkova_zhoda > 80;

        entity.setPrve_meno("Tomáš");
        entity.setPriezvisko("Kováč");
        entity.setUlica("Kpt.Nálepku");
        entity.setObec("Košice");
        entity.setNanoId(NanoIdUtils.randomNanoId());
        FindRequestDTO = new FindRequestDTO("Tomáš", "Kováč", "Kapitána Nálepku", "Košice", NanoIdUtils.randomNanoId());
        celkova_zhoda = pohladavkaService.calculateMatch(FindRequestDTO,entity, celkova_zhoda);
        System.out.println("celkova zhoda = " +celkova_zhoda);
        assert celkova_zhoda > 80;

        entity.setPrve_meno("Tomáš");
        entity.setPriezvisko("Kovács");
        entity.setUlica("Komenského 41");
        entity.setObec("Košice");
        entity.setNanoId(NanoIdUtils.randomNanoId());
        FindRequestDTO = new FindRequestDTO("Tomáš", "Kováč", "Komenského 41", "Košice", NanoIdUtils.randomNanoId());
        celkova_zhoda = pohladavkaService.calculateMatch(FindRequestDTO,entity, celkova_zhoda);
        System.out.println("celkova zhoda = " +celkova_zhoda);
        assert celkova_zhoda < 80;



        entity.setPrve_meno("Atila");
        entity.setPriezvisko("Grónsky");
        entity.setUlica("Komenského 41");
        entity.setObec("Košice");
        entity.setNanoId(NanoIdUtils.randomNanoId());
        FindRequestDTO = new FindRequestDTO("Atila", "Grónsky", "Tomášikova 41", "Košice", NanoIdUtils.randomNanoId());
        celkova_zhoda = pohladavkaService.calculateMatch(FindRequestDTO,entity, celkova_zhoda);
        System.out.println("celkova zhoda = " +celkova_zhoda);
        assert celkova_zhoda < 80;

        entity.setPrve_meno("Peter");
        entity.setPriezvisko("Grónsky");
        entity.setUlica("Komenského 41");
        entity.setObec("Košice");
        entity.setNanoId(NanoIdUtils.randomNanoId());
        FindRequestDTO = new FindRequestDTO("Jano", "Grónsky", "Tomášikova 41", "Košice", NanoIdUtils.randomNanoId());
        celkova_zhoda = pohladavkaService.calculateMatch(FindRequestDTO,entity, celkova_zhoda);
        System.out.println("celkova zhoda = " +celkova_zhoda);
        assert celkova_zhoda < 80;

        entity.setPrve_meno("Peter");
        entity.setPriezvisko("Grónsky");
        entity.setUlica("Komenského 41");
        entity.setObec("Košice");
        entity.setNanoId(NanoIdUtils.randomNanoId());
        FindRequestDTO = new FindRequestDTO("Jano", "Grónsky", "Tomášikova 41", "Košice", NanoIdUtils.randomNanoId());
        celkova_zhoda = pohladavkaService.calculateMatch(FindRequestDTO,entity, celkova_zhoda);
        System.out.println("celkova zhoda = " +celkova_zhoda);
        assert celkova_zhoda < 80;
    }


}
