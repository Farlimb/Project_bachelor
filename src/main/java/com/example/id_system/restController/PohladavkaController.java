package com.example.id_system.restController;

import com.example.id_system.dto.CreateRequestDTO;
import com.example.id_system.dto.FindRequestDTO;
import com.example.id_system.dto.FindResponseDTO;
import com.example.id_system.dto.UpdateRequestDTO;
import com.example.id_system.services.PohladavkaService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
public class PohladavkaController {
    private final PohladavkaService pohladavkaService;

    public PohladavkaController(PohladavkaService pohladavkaService) {
        this.pohladavkaService = pohladavkaService;
    }

    @PostMapping(value="/find",produces = MediaType.APPLICATION_JSON_VALUE) //Web funkcia na vyhľadanie podľa poslaných vstupných údajov
    public List<FindResponseDTO> findByParams(@RequestBody FindRequestDTO params){
        return pohladavkaService.FindByParams(params);
    }

    @PostMapping(value="/findBest",produces = MediaType.APPLICATION_JSON_VALUE) //Web funkcia na vyhľadanie podľa poslaných vstupných údajov
    public FindResponseDTO findByBestParams(@RequestBody FindRequestDTO params){
        return pohladavkaService.FindBestByParams(params);
    }

    @PostMapping(value="/update",produces = MediaType.APPLICATION_JSON_VALUE) //Web funkcia na vyhľadanie a vytvorenie nového záznamu podľa poslaných vstupných údajov s tým istým identifikátorom
    @ResponseBody
    public Set<FindResponseDTO> updateByparams(@RequestBody UpdateRequestDTO params){

         return Collections.singleton(pohladavkaService.UpdateByParams(params));
    }

    @PostMapping(value="/create",produces = MediaType.APPLICATION_JSON_VALUE) //Web funkcia na vytvorenie nového záznamu podľa poslaných vstupných údajov
    public Set<FindResponseDTO> createByparams(@RequestBody CreateRequestDTO params){
        return Collections.singleton(pohladavkaService.CreateByParams(params));
    }

    @PostMapping(value="/delete",produces = MediaType.APPLICATION_JSON_VALUE) //Web funkcia na vymazanie nového záznamu podľa poslaných vstupných údajov
    public String deleteByparams(@RequestBody FindRequestDTO params){
        return pohladavkaService.DeleteByParams(params);
    }
    @GetMapping("/intializedb") //Web funkcia na inicializáciu údajov v databáze do príslušných stĺpcov
    public void convert(){
        pohladavkaService.ConvertAll();
    }

}
