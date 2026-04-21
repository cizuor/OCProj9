package com.yourcaryourway.chatpoc;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.yourcaryourway.chatpoc.model.Voiture;
import com.yourcaryourway.chatpoc.repository.VoitureRepository;


@Component
public class DataInitializer implements CommandLineRunner {

	
	private final VoitureRepository voitureRepository;

	public DataInitializer(VoitureRepository voitureRepository) {
		super();
		this.voitureRepository = voitureRepository;
	}
	
	
	@Override
    public void run(String... args) throws Exception {
        if (voitureRepository.count() == 0) {
            Voiture clio = new Voiture();
            clio.setNom("Renault Clio");
            clio.setPrixJournalier(new BigDecimal("45.00"));
            
            Voiture twingo = new Voiture();
            twingo.setNom("Renault Twingo");
            twingo.setPrixJournalier(new BigDecimal("35.00"));
            
            Voiture p208 = new Voiture();
            p208.setNom("Peugeot 208");
            p208.setPrixJournalier(new BigDecimal("42.00"));
            
            Voiture p308 = new Voiture();
            p308.setNom("Peugeot 308");
            p308.setPrixJournalier(new BigDecimal("52.00"));
            
            Voiture p911 = new Voiture();
            p911.setNom("porshe 911");
            p911.setPrixJournalier(new BigDecimal("117.00"));

            voitureRepository.saveAll(List.of(clio, twingo, p208, p308, p911));
            System.out.println(">> Inventaire initial de voitures créé !");
        }
    }
	
	
}
