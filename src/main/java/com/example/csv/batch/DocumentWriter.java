package com.example.csv.batch;

import com.example.csv.domain.Tiers;
import com.example.csv.services.TiersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Slf4j
public class DocumentWriter implements ItemWriter<Tiers> {

        private TiersService tiersService;


        public void write(List<?extends Tiers> tiers){
            tiers.stream().forEach(tier->{
            log.info("Enregistrement des lignes tiers dans la base de données",tier);
            tiersService.save(tier);

        });
        }
        }
