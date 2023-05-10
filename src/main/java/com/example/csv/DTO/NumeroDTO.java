package com.example.csv.DTO;

import lombok.Data;

@Data
public class NumeroDTO {

    private String numero;
    private Long count;

    public NumeroDTO(String numero, Long count) {
        this.numero = numero;
        this.count = count;
    }
}
