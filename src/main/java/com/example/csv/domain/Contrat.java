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
    private String Num_dossierKPS;
    private String Num_CP;
    private String Raison_Social;
    private String Id_Tiers;
    private String Num_DC;
    private String Num_SDC;
    private String Num_CIR;
    private String Num_SIREN;
    private String Ref_Collaborative;
    private String Code_Produit;
    private String Identifiant_de_offre_comm;
    private String Chef_de_File;
    private String Num_OVI;
    private String Num_RUM;
    private String TypeEnergie;
    private String Produit_Comm;
    private String Produit;
    private String Phase;
    private String Montant_pret;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> customAttributes;

    public Contrat(Object o, String numDossier, String numCp, String raisonSocial, String idTiers, String numDc, String numSdc, String numCir, String numSiren, String refColl, String codeProduit, String idDeOffreComm, String chefDeFile, String numOvi, String numRum, String typeenregie, String produitComm, String produit, String phase, String montantPret) {
    }
}
