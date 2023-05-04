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
    private String Num_dossierKPS;
    private String Num_CP;
    private String Raison_Social;
    private String Id_Tiers;
    
    private String Num_SIREN;

    private String Code_Produit;

    private String Chef_de_File;

    private String Produit;

    private String Montant_pret;

}
