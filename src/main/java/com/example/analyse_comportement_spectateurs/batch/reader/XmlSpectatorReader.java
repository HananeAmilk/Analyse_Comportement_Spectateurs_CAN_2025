package com.example.analyse_comportement_spectateurs.batch.reader;

import com.example.analyse_comportement_spectateurs.model.Dtos.SeatLocationDto;
import com.example.analyse_comportement_spectateurs.model.Dtos.SpectatorEntryDto;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

@Component
public class XmlSpectatorReader {

    public StaxEventItemReader<SpectatorEntryDto> xmlReader() {
        // Configuration du marshaller JAXB
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setClassesToBeBound(
                SpectatorEntryDto.class,
                SeatLocationDto.class
        );

        return new StaxEventItemReaderBuilder<SpectatorEntryDto>()
                .name("xmlSpectatorReader")
                .resource(new ClassPathResource("data/spectators.xml"))
                .addFragmentRootElements("spectatorEntry")
                .unmarshaller(marshaller)
                .build();
    }
}