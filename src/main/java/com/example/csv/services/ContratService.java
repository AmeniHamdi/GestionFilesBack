package com.example.csv.services;

import com.example.csv.DTO.ProduitDTO;
import com.example.csv.domain.Contrat;
import com.example.csv.domain.GetAllType;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ContratService {
    Contrat save(Contrat contrat);

    void saveResultsFromOcr(Map<String, String> data);

    void saveFile(MultipartFile file);

    public List<Contrat> getAllContrat();

    Contrat getContrat(Long id);

    void delete(Long id);
    boolean update(Contrat contrat);
    GetAllType<Contrat> getAllContrats(Integer pageNo, Integer pageSize, String sortBy, boolean asc);
     Long countContracts();

    List<ProduitDTO> countContractsByProduit();

}
