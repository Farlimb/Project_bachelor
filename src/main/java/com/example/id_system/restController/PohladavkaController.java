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

/**
 * Táto trieda obsahuje koncové body API na vykonávanie rôznych operácií súvisiacich s entitou "Pohladavka".
 */
@RestController
public class PohladavkaController {
    private final PohladavkaService pohladavkaService;

    public PohladavkaController(PohladavkaService pohladavkaService) {
        this.pohladavkaService = pohladavkaService;
    }

    /**
     * Nájde entity 'Pohladavka' na základe zadaných parametrov vyhľadávania.
     *
     * @param params Vyhľadávacie parametre pre nájdenie entít 'Pohladavka'.
     * @return Zoznam objektov "FindResponseDTO" obsahujúcich nájdené entity "Pohladavka".
     */
    @PostMapping(value="/find",produces = MediaType.APPLICATION_JSON_VALUE) //Web funkcia na vyhľadanie podľa poslaných vstupných údajov
    public List<FindResponseDTO> findByParams(@RequestBody FindRequestDTO params){
        return pohladavkaService.FindByParams(params);
    }

    /**
     * Nájde najvhodnejšiu entitu 'Pohladavka' na základe zadaných parametrov vyhľadávania.
     *
     * @param params Parametre vyhľadávania pre nájdenie najlepšie zodpovedajúcej entity 'Pohladavka'.
     * @return Objekt "FindResponseDTO" obsahujúci najlepšie zodpovedajúci subjekt "Pohladavka".
     */
    @PostMapping(value="/findBest",produces = MediaType.APPLICATION_JSON_VALUE) //Web funkcia na vyhľadanie podľa poslaných vstupných údajov
    public FindResponseDTO findByBestParams(@RequestBody FindRequestDTO params){
        return pohladavkaService.FindBestByParams(params);
    }
    /**
     * Aktualizuje entitu 'Pohladavka' na základe zadaných parametrov vyhľadávania a vytvorí nový záznam s rovnakým identifikátorom.
     *
     * @param params Parametre aktualizácie pre aktualizáciu entity "Pohladavka".
     * @return Súbor objektov "FindResponseDTO" obsahujúcich aktualizovanú entitu "Pohladavka".
     */
    @PostMapping(value="/update",produces = MediaType.APPLICATION_JSON_VALUE) //Web funkcia na vyhľadanie a vytvorenie nového záznamu podľa poslaných vstupných údajov s tým istým identifikátorom
    @ResponseBody
    public Set<FindResponseDTO> updateByParams(@RequestBody UpdateRequestDTO params){
         return Collections.singleton(pohladavkaService.UpdateByParams(params));
    }
    /**
     * Vytvorí novú entitu 'Pohladavka' na základe zadaných parametrov.
     *
     * @param params Parametre pre vytvorenie novej entity 'Pohladavka'.
     * @return Sada objektov "FindResponseDTO" obsahujúca vytvorenú entitu "Pohladavka".
     */
    @PostMapping(value="/create",produces = MediaType.APPLICATION_JSON_VALUE) //Web funkcia na vytvorenie nového záznamu podľa poslaných vstupných údajov
    public Set<FindResponseDTO> createByParams(@RequestBody CreateRequestDTO params){
        return Collections.singleton(pohladavkaService.CreateByParams(params));
    }
    /**
     * Odstráni entitu 'Pohladavka' na základe zadaných parametrov vyhľadávania.
     *
     * @param params Parametre vyhľadávania na vymazanie entity 'Pohladavka'.
     * @return Reťazec označujúci výsledok operácie vymazania.
     */
    @PostMapping(value="/delete",produces = MediaType.APPLICATION_JSON_VALUE) //Web funkcia na vymazanie nového záznamu podľa poslaných vstupných údajov
    public String deleteByParams(@RequestBody FindRequestDTO params){
        return pohladavkaService.DeleteByParams(params);
    }
    /**
     * Inicializuje údaje v databáze a naplní príslušné stĺpce.
     */
    @GetMapping("/intializedb") //Web funkcia na inicializáciu údajov v databáze do príslušných stĺpcov
    public void convert(){
        pohladavkaService.ConvertAll();
    }

}
