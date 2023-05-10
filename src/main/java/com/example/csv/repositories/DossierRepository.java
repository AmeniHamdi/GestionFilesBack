package com.example.csv.repositories;

import com.example.csv.domain.Dossier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface DossierRepository extends JpaRepository<Dossier, Long>{
    @Query("SELECT c.Produit, COUNT(c.Produit) FROM Contrat c GROUP BY c.Produit")
    List<Object[]> countByPr();

}
