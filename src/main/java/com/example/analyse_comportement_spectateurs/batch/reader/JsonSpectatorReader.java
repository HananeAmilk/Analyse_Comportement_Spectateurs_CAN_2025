package com.example.analyse_comportement_spectateurs.batch.reader;

import com.example.analyse_comportement_spectateurs.model.Dtos.EntryDto;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class JsonSpectatorReader {

    @Bean
    public JsonItemReader<EntryDto> jsonReader() {
        return new JsonItemReaderBuilder<EntryDto>()
                .name("jsonSpectatorReader")
                .resource(new FileSystemResource("data/spectators.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(EntryDto.class))
                .build();
    }
}
