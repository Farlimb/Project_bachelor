package com.example.bakalarka_jpa.restController;

import com.example.bakalarka_jpa.entities.PohladavkaEntity;
import com.example.bakalarka_jpa.services.PohladavkaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PohladavkaController {
    private final PohladavkaService pohladavkaService;

    public PohladavkaController(PohladavkaService pohladavkaService) {
        this.pohladavkaService = pohladavkaService;
    }

//    @GetMapping("/{meno}/{priezvisko/{obec}/{ulica}")
//    public PohladavkaEntity findByNameAndAddress(@PathVariable String meno, String priezvisko, String obec, String ulica){
//        PohladavkaEntity pohladavkaEntity = pohladavkaService.
//        return pohladavkaEntity;
//    }
    @GetMapping("/{id}")
    public PohladavkaEntity findById(@PathVariable int id){
        PohladavkaEntity pohladavkaEntity = pohladavkaService.FindById(id).orElse(null);
        return pohladavkaEntity;
    }
    @GetMapping("/phonetic")
    public void convert(){
        pohladavkaService.ConvertAll();
    }

}
