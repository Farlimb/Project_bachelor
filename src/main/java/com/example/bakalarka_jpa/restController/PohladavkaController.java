package com.example.bakalarka_jpa.restController;

import com.example.bakalarka_jpa.dto.CreateRequestDTO;
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

    @PostMapping("/find") //Web funkcia na vyhľadanie podľa poslaných vstupných údajov
    public Set<FindResponseDTO> findByparams(@RequestBody FindRequestDTO params){
        return pohladavkaService.FindByParams(params);
    }

    @PostMapping("/update") //Web funkcia na vyhľadanie a vytvorenie nového záznamu podľa poslaných vstupných údajov s tým istým identifikátorom
    public String updateByparams(@RequestBody UpdateRequestDTO params){
        return pohladavkaService.UpdateByParams(params);
    }

    @PostMapping("/create") //Web funkcia na vytvorenie nového záznamu podľa poslaných vstupných údajov
    public String createByparams(@RequestBody CreateRequestDTO params){
        return pohladavkaService.CreateByParams(params);
    }

    @PostMapping("/delete") //Web funkcia na vymazanie nového záznamu podľa poslaných vstupných údajov
    public String deleteByparams(@RequestBody FindRequestDTO params){
        return pohladavkaService.DeleteByParams(params);
    }
    @GetMapping("/intializedb") //Web funkcia na inicializáciu údajov v databáze do príslušných stĺpcov
    public void convert(){
        pohladavkaService.ConvertAll();
    }

}
