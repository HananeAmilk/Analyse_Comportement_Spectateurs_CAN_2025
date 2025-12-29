package com.example.analyse_comportement_spectateurs.batch.reader;

import com.example.analyse_comportement_spectateurs.model.Dtos.SpectatorEntryDto;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class JsonSpectatorReader {

    @Bean(name = "jsonItemReader")
    public JsonItemReader<SpectatorEntryDto> jsonReader() {
        return new JsonItemReaderBuilder<SpectatorEntryDto>()
                .name("jsonItemReader")

                .resource(new ClassPathResource("data/spectators.json"))

                .jsonObjectReader(new JacksonJsonObjectReader<>(SpectatorEntryDto.class))
                .build();
    }
}
