package com.example.csv.domain;

import com.example.csv.config.HashMapConverter;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

import lombok.RequiredArgsConstructor;


import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

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

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> customAttributes;

    public Tiers(Object o, String s, String inga, String s1, String chviz) {
    }
}
