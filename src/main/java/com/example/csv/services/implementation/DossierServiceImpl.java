package com.example.csv.services.implementation;

import com.example.csv.domain.Contrat;
import com.example.csv.domain.Dossier;
import com.example.csv.domain.GetAllType;
import com.example.csv.domain.Tiers;
import com.example.csv.helper.CSVHelper;
import com.example.csv.repositories.DossierRepository;
import com.example.csv.services.DossierService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DossierServiceImpl implements DossierService {
    @Autowired
    private final DossierRepository dosRepo;

    @Override
    public void saveFile(MultipartFile file) {
        try {
            List<Dossier> dossiers = CSVHelper.csvToDossiers(file.getInputStream());
            dosRepo.saveAll(dossiers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void saveResultsFromOcr(Map<String, String> data) {
            Dossier dossier = CSVHelper.ocrToDossiers(data);
            dosRepo.save(dossier);
    }

    public List<Dossier> getAllDossiers() {
        return dosRepo.findAll();
    }

    @Override
    public Dossier getDossier(Long id) {
        return dosRepo.findById(id).get();
    }

    @Override
    public void delete(Long id) {
        dosRepo.deleteById(id);
    }

    @Override
    public Dossier save(Dossier dossier) {
        Dossier dossier1 = dosRepo.save(dossier);
        return dossier1;
    }


    @Override
    public boolean update(Dossier updatedDossier) {
       /* Dossier toUpdate = dosRepo.findById(updatedDossier.getId()).orElse(null);
        if (toUpdate == null) { return false; }

        // save the updated version*/
        dosRepo.save(updatedDossier);
        return true;
    }

    @Override
    public GetAllType<Dossier> getAllDossiers(Integer pageNo, Integer pageSize, String sortBy, boolean asc) {
        Sort.Direction direction = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
        Long count = dosRepo.count();

        Page<Dossier> pagedResult = dosRepo.findAll(paging);

        GetAllType<Dossier>  result= new GetAllType<>();
        result.setCount(count);
        result.setRows(pagedResult.getContent());
        return  result;
    }

    @Override
    public List<Dossier> findDossierWithSorting(String field) {
        return dosRepo.findAll(Sort.by(Sort.Direction.ASC,field));
    }

    @Override
    public Long countDossier() {
        return dosRepo.count();
    }


}
