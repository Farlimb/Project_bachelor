package com.example.bakalarka_jpa.restController;

import com.example.bakalarka_jpa.dto.FindRequestDTO;
import com.example.bakalarka_jpa.dto.FindResponseDTO;
import com.example.bakalarka_jpa.dto.UpdateRequestDTO;
import com.example.bakalarka_jpa.entities.PohladavkaEntity;
import com.example.bakalarka_jpa.services.PohladavkaService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
    @PostMapping("/find")
    public Set<FindResponseDTO> findByparams(@RequestBody FindRequestDTO params){
        return pohladavkaService.FindByParams(params);
    }

    @PostMapping("/update")
    public String updateByparams(@RequestBody UpdateRequestDTO params){
        return pohladavkaService.UpdateByParams(params);
    }

    @GetMapping("/phonetic")
    public void convert(){
        pohladavkaService.ConvertAll();
    }

}
