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
public class Dossier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dossier_DC;
    private String numero;
    private String listSDC;
    private String n_DPS;
    private String montant_du_pres;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> customAttributes;


    public Dossier(Long id) {
        this.id=id;

    }
}
