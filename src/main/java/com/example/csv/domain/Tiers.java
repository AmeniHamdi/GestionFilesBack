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

public class Tiers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numero;
    private String nom;
    private String siren;
    private String ref_mandat;

}
