package com.example.csv.services;

import com.example.csv.DTO.NumeroDTO;
import com.example.csv.DTO.ProduitDTO;
import com.example.csv.domain.GetAllType;
import com.example.csv.domain.Tiers;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TiersService {
    public List<Tiers> searchTiers(String numero, String nom, String siren, String refMandat);

    Tiers save(Tiers tiers);

    void saveResultsFromOcr(Map<String, String> data);

    void saveFile(MultipartFile file);

    List<Tiers> getAllTiers();

    Tiers getTiers(Long id);

    void delete(Long id);



    boolean update(Tiers tiers);
    GetAllType<Tiers> getAllTiers(Integer pageNo, Integer pageSize, String sortBy, boolean asc, String searchTerm);


    Long countTiers();

    List<NumeroDTO> countTiersByNumero();

}
