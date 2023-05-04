package com.example.csv.DTO;


import lombok.Data;

@Data
public class ProduitDTO {
    private String produit;
    private Long count;

    public ProduitDTO(String produit, Long count) {
        this.produit = produit;
        this.count = count;
    }
}
