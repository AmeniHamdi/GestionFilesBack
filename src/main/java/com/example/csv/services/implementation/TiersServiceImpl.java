package com.example.csv.services.implementation;

import com.example.csv.DTO.NumeroDTO;
import com.example.csv.DTO.ProduitDTO;
import com.example.csv.domain.GetAllType;
import com.example.csv.domain.Tiers;
import com.example.csv.helper.CSVHelper;
import com.example.csv.repositories.TiersRepository;
import com.example.csv.services.TiersService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Array;
import java.util.ArrayList;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TiersServiceImpl implements TiersService {

    @Autowired
    private final TiersRepository tiersRepo;

    @Override
    public List<Tiers> searchTiers(String numero, String nom, String siren, String refMandat) {
        Specification<Tiers> specification = Specification.where(null);

        if (numero != null && !numero.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("numero"), "%" + numero + "%"));
        }

        if (nom != null && !nom.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("nom"), nom));
        }

        if (siren != null && !siren.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("siren"), "%" + siren + "%"));
        }

        if (refMandat != null && !refMandat.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("refMandat"), "%" + refMandat + "%"));
        }

        return tiersRepo.findAll(specification);
    }


    @Override
    public Tiers save(Tiers tiers) {

        return tiersRepo.save(tiers);
    }

    @Override
    public void saveResultsFromOcr(Map<String, String> data) {
            Tiers tiers = CSVHelper.ocrToTiers(data);
            tiersRepo.save(tiers);
    }

    @Override
    public void saveFile(MultipartFile file) {
        try {
            List<Tiers> tiers = CSVHelper.csvToTiers(file.getInputStream());
            tiersRepo.saveAll(tiers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Tiers> getAllTiers() {
        return tiersRepo.findAll();
    }

    @Override
    public Tiers getTiers(Long id) {
        return tiersRepo.findById(id).get();
    }

    @Override
    public void delete(Long id) {
        tiersRepo.deleteById(id);
    }


    @Override
    public boolean update(Tiers updatedTiers) {
        /*Tiers toUpdate = tiersRepo.findById(updatedTiers.getId()).orElse(null);
        if (toUpdate == null) {
            return false;
        }

        // save the updated version*/
        tiersRepo.save(updatedTiers);
        return true;


    }

    @Override
    public GetAllType<Tiers> getAllTiers(Integer pageNo, Integer pageSize, String sortBy, boolean asc,
                                         String searchTerm) {
        Sort.Direction direction = asc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Long count;
        Page<Tiers> pagedResult;
        if (searchTerm == null || searchTerm.equals("")) {
            count = tiersRepo.count();
            pagedResult = tiersRepo.findAll(paging);
        } else {
            count = tiersRepo.countBySearchTerm(searchTerm.toUpperCase());
            pagedResult = tiersRepo.findAll(searchTerm.toUpperCase()
                    , paging);
        }

        GetAllType<Tiers>  result= new GetAllType<>();
        result.setCount(count);
        result.setRows(pagedResult.getContent());
        return  result;
    }
    @Override
    public Long countTiers(){
        return tiersRepo.count();
    }

   public List<NumeroDTO> countTiersByNumero(){
       List<Object[]> results = tiersRepo.countByNumero();
       List<NumeroDTO> counts = new ArrayList<>();

       for (Object[] result : results) {
           String Numero = (String) result[0];
           Long count = (Long) result[1];
           counts.add(new NumeroDTO(Numero, count));
       }

       return counts;
    }


}
