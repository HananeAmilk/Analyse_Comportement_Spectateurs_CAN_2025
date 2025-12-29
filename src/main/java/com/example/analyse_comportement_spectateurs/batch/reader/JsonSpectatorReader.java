package com.example.analyse_comportement_spectateurs.batch.reader;

import com.example.analyse_comportement_spectateurs.model.Dtos.SpectatorEntryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        JacksonJsonObjectReader<SpectatorEntryDto> jsonObjectReader =
                new JacksonJsonObjectReader<>(objectMapper, SpectatorEntryDto.class);

        return new JsonItemReaderBuilder<SpectatorEntryDto>()
                .name("jsonItemReader")
                .resource(new ClassPathResource("data/spectators.json"))
                .jsonObjectReader(jsonObjectReader)
                .build();
    }
}
