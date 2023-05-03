package com.example.csv.services;

import com.example.csv.domain.Contrat;
import com.example.csv.domain.Dossier;
import com.example.csv.domain.GetAllType;
import com.example.csv.domain.Tiers;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface DossierService {
    void saveFile(MultipartFile file);

    void saveResultsFromOcr(Map<String, String> data);

    List<Dossier> getAllDossiers();

    Dossier getDossier(Long id);

    void delete(Long id);

    Dossier save(Dossier dossier);

    boolean update(Dossier dossier);

    GetAllType<Dossier> getAllDossiers(Integer pageNo, Integer pageSize, String sortBy, boolean asc);

    public List<Dossier> findDossierWithSorting(String field);


}
