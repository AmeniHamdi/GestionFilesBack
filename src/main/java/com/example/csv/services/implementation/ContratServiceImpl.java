package com.example.csv.services.implementation;

import com.example.csv.domain.Contrat;
import com.example.csv.domain.Dossier;
import com.example.csv.domain.GetAllType;
import com.example.csv.domain.Tiers;
import com.example.csv.helper.CSVHelper;
import com.example.csv.repositories.ContratRepository;
import com.example.csv.services.ContratService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ContratServiceImpl implements ContratService {

    @Autowired
    private final ContratRepository contratRepo;


    @Override
    public Contrat save(Contrat contrat) {
        contratRepo.save(contrat);
        return contrat;
    }

    @Override
    public void saveFile(MultipartFile file) {

        try {
            List<Contrat> contrats = CSVHelper.csvToContrats(file.getInputStream());
            contratRepo.saveAll(contrats);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
        public List<Contrat> getAllContrat() {
        return contratRepo.findAll();
    }

    @Override
    public Contrat getContrat(Long id) {
        return contratRepo.findById(id).get();
    }

    @Override
    public void delete(Long id) {
        contratRepo.deleteById(id);
    }

    @Override
    public boolean update(Contrat updatedContrat) {
       /* Contrat toUpdate = contratRepo.findById(updatedContrat.getId()).orElse(null);
        if (toUpdate == null) { return false; }
        // save the updated version*/
        contratRepo.save(updatedContrat);
        return true;
    }

    @Override
    public GetAllType<Contrat> getAllContrats(Integer pageNo, Integer pageSize, String sortBy, boolean asc) {
        Sort.Direction direction = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Long count = contratRepo.count();

        Page<Contrat> pagedResult = contratRepo.findAll(paging);

        GetAllType<Contrat>  result= new GetAllType<>();
        result.setCount(count);
        result.setRows(pagedResult.getContent());
        return  result;
    }


}
