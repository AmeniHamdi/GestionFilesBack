package com.example.csv.repositories;

import com.example.csv.domain.Tiers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TiersRepository extends JpaRepository<Tiers,Long>, JpaSpecificationExecutor<Tiers> {

    Page<Tiers> findAll(Pageable pageable);

    @Query("SELECT count(*) FROM Tiers t WHERE CONCAT(UPPER(t.id), UPPER(t.nom), UPPER(t.numero), UPPER(t.siren), UPPER(t.ref_mandat))" +
            " LIKE %:searchTerm%")
    Long countBySearchTerm(@Param("searchTerm") String searchTerm);

    @Query("SELECT t FROM Tiers t WHERE CONCAT(UPPER(t.id), UPPER(t.nom), UPPER(t.numero), UPPER(t.siren), UPPER(t.ref_mandat))" +
            " LIKE %:searchTerm%")
    Page<Tiers> findAll(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT c.numero, COUNT(c.numero) FROM Tiers c GROUP BY c.numero")
    List<Object[]> countByNumero();

}
