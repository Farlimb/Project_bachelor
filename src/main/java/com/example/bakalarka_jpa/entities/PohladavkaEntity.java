package com.example.bakalarka_jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pohladavka")
public class PohladavkaEntity {
    @Column(name = "prve_meno_upravene",columnDefinition="TEXT")
    private String prveMenoUpravene;
    @Column(name = "priezvisko_upravene",columnDefinition="TEXT")
    private String priezviskoUpravene;
    @Column(name = "prve_meno_upravene_kolner")
    private String prveMenoUpraveneKolner;
    @Column(name = "priezvisko_upravene_kolner")
    private String priezviskoUpraveneKolner;
    @Column(name = "obec")
    private String obec;
    @Column(name = "ulica_cislo")
    private String ulica;
    @Column(name = "prve_meno")
    private String prve_meno;
    @Column(name = "priezvisko")
    private String priezvisko;
    @Column(name = "nano-id")
    private String nanoId;

    @Id
    @Column(name = "id")
    private int id;

    public String getPrve_meno() {
        return prve_meno;
    }

    public void setPrve_meno(String prve_meno) {
        this.prve_meno = prve_meno;
    }

    public String getPriezvisko() {
        return priezvisko;
    }

    public void setPriezvisko(String priezvisko) {
        this.priezvisko = priezvisko;
    }

    public String getObec() {
        return obec;
    }

    public void setObec(String obec) {
        this.obec = obec;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }


    public String getPrveMenoUpravene() {
        return prveMenoUpravene;
    }

    public void setPrveMenoUpravene(String prveMenoUpravene) {
        this.prveMenoUpravene = prveMenoUpravene;
    }

    public String getPriezviskoUpravene() {
        return priezviskoUpravene;
    }

    public void setPriezviskoUpravene(String priezviskoUpravene) {
        this.priezviskoUpravene = priezviskoUpravene;
    }

    public String getPrveMenoUpraveneKolner() {
        return prveMenoUpraveneKolner;
    }

    public void setPrveMenoUpraveneKolner(String prveMenoUpraveneKolner) {
        this.prveMenoUpraveneKolner = prveMenoUpraveneKolner;
    }

    public String getPriezviskoUpraveneKolner() {
        return priezviskoUpraveneKolner;
    }

    public void setPriezviskoUpraveneKolner(String priezviskoUpraveneKolner) {
        this.priezviskoUpraveneKolner = priezviskoUpraveneKolner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
