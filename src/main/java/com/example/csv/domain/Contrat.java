package com.example.csv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

import lombok.RequiredArgsConstructor;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    public Contrat(Long id) {
        this.id = id ;
    }
}
