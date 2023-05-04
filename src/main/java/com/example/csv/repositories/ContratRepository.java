package com.example.csv.repositories;

import com.example.csv.DTO.ProduitDTO;
import com.example.csv.domain.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;


public interface ContratRepository extends JpaRepository<Contrat,Long> {

    @Query("SELECT c.Produit, COUNT(c.Produit) FROM Contrat c GROUP BY c.Produit")
    List<Object[]> countByProduit();
}
