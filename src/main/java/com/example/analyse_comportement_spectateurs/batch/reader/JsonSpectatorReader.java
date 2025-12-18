package com.example.analyse_comportement_spectateurs.batch.reader;

import com.example.analyse_comportement_spectateurs.model.SpectatorEntry;
import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectReader;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.batch.infrastructure.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class JsonSpectatorReader {

    @Bean
    public JsonItemReader<SpectatorEntry> jsonReader() {
        return new JsonItemReaderBuilder<SpectatorEntry>()
                .name("jsonSpectatorReader")
                .resource(new FileSystemResource("data/spectators.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(SpectatorEntry.class))
                .build();
    }
}
