package com.example.csv.DTO;

import com.example.csv.config.HashMapConverter;
import lombok.Data;

import javax.persistence.Convert;
import java.util.Map;

@Data
public class TiersDTO {

    private String numero;
    private String nom;
    private String siren;
    private String ref_mandat;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> customAttributes;
}
