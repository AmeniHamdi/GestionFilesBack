package com.example.csv.DTO;

import com.example.csv.config.HashMapConverter;
import lombok.Data;

import javax.persistence.Convert;
import java.util.Map;

@Data
public class DossierDTO {

    private String dossier_DC;
    private String listSDC;
    private String n_DPS;
    private String montant_du_pres;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> customAttributes;
}
