package com.example.csv.batch;
import org.springframework.batch.item.ItemProcessor;
import com.example.csv.domain.Tiers;

public class DocumentProcessor implements ItemProcessor<Tiers, Tiers>{
    @Override
    public Tiers process(Tiers tiers) {
        tiers.setNom(tiers.getNom());
        tiers.setSiren(tiers.getSiren());
        tiers.setNumero(tiers.getNumero());
        tiers.setId(tiers.getId());
        return tiers;
    }
}
