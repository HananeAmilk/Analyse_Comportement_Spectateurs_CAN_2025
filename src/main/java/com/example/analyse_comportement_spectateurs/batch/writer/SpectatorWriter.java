package com.example.analyse_comportement_spectateurs.batch.writer;

import com.example.analyse_comportement_spectateurs.model.Entities.Entry;
import com.example.analyse_comportement_spectateurs.model.Entities.Spectator;
import com.example.analyse_comportement_spectateurs.repositories.EntryRepository;
import com.example.analyse_comportement_spectateurs.repositories.SpectatorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Writer pour persister les spectateurs et leurs entrées
 * Sauvegarde dans la base de données de manière transactionnelle
 */
@Component
public class SpectatorWriter implements ItemWriter<Map<String, Object>> {

    private static final Logger logger = LoggerFactory.getLogger(SpectatorWriter.class);

    @Autowired
    private SpectatorRepository spectatorRepository;

    @Autowired
    private EntryRepository entryRepository;

    /**
     * Écrit un chunk d'items dans la base de données
     * Gère la sauvegarde des spectateurs et de leurs entrées
     *
     * @param chunk liste des items à écrire
     */
    @Override
    @Transactional
    public void write(Chunk<? extends Map<String, Object>> chunk) throws Exception {

        List<Spectator> spectatorsToSave = new ArrayList<>();
        List<Entry> entriesToSave = new ArrayList<>();

        // Extraction des données du chunk
        for (Map<String, Object> item : chunk) {
            if (item != null) {
                Spectator spectator = (Spectator) item.get("spectator");
                Entry entry = (Entry) item.get("entry");

                if (spectator != null) {
                    spectatorsToSave.add(spectator);
                }

                if (entry != null) {
                    entriesToSave.add(entry);
                }
            }
        }

        // Sauvegarde en batch
        try {
            // Sauvegarde des spectateurs
            if (!spectatorsToSave.isEmpty()) {
                List<Spectator> savedSpectators = spectatorRepository.saveAll(spectatorsToSave);
                logger.info("Sauvegardé {} spectateurs", savedSpectators.size());
            }

            // Sauvegarde des entrées
            if (!entriesToSave.isEmpty()) {
                List<Entry> savedEntries = entryRepository.saveAll(entriesToSave);
                logger.info("Sauvegardé {} entrées", savedEntries.size());
            }

        } catch (Exception e) {
            logger.error("Erreur lors de la sauvegarde: {}", e.getMessage(), e);
            throw e;
        }
    }
}

//// ============= Alternative: JPA Writer (Simplifié) =============
//
//package com.ensa.can2025.writer;
//
//import com.ensa.can2025.model.entity.Entry;
//import com.ensa.can2025.model.entity.Spectator;
//import org.springframework.batch.item.database.JpaItemWriter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import jakarta.persistence.EntityManagerFactory;
//
///**
// * Configuration alternative avec JpaItemWriter
// * Plus simple mais moins de contrôle
// */
//@Component
//public class JpaWriterConfig {
//
//    /**
//     * Writer JPA pour Spectator
//     */
//    @Bean(name = "spectatorJpaWriter")
//    public JpaItemWriter<Spectator> spectatorJpaWriter(EntityManagerFactory entityManagerFactory) {
//        JpaItemWriter<Spectator> writer = new JpaItemWriter<>();
//        writer.setEntityManagerFactory(entityManagerFactory);
//        return writer;
//    }
//
//    /**
//     * Writer JPA pour Entry
//     */
//    @Bean(name = "entryJpaWriter")
//    public JpaItemWriter<Entry> entryJpaWriter(EntityManagerFactory entityManagerFactory) {
//        JpaItemWriter<Entry> writer = new JpaItemWriter<>();
//        writer.setEntityManagerFactory(entityManagerFactory);
//        return writer;
//    }
//}