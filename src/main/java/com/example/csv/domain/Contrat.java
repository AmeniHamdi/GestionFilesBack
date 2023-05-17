package com.example.csv.domain;

import com.example.csv.config.HashMapConverter;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

import lombok.RequiredArgsConstructor;


import javax.persistence.*;
import java.util.Map;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor

public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String NumCP;
    private String RaisonSocial;
    private String IdTiers;

    private String NumSIREN;
    private String Produit;
    private String Phase;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> customAttributes;

    public Contrat(Long id) {
        this.id = id;
    }
}


