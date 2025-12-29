package com.example.analyse_comportement_spectateurs.config;

import com.example.analyse_comportement_spectateurs.batch.reader.JsonSpectatorReader;
import com.example.analyse_comportement_spectateurs.batch.reader.XmlSpectatorReader;
import com.example.analyse_comportement_spectateurs.model.Dtos.SpectatorEntryDto;
import com.example.analyse_comportement_spectateurs.batch.processor.SpectatorProcessor;
import com.example.analyse_comportement_spectateurs.batch.writer.SpectatorWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

/**
 * Configuration Spring Batch SANS Listeners
 * Version simplifiée pour mini-projet
 */
@Configuration
public class BatchConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JsonSpectatorReader jsonReader;

    @Autowired
    private XmlSpectatorReader xmlReader;

    @Autowired
    private SpectatorProcessor processor;

    @Autowired
    private SpectatorWriter writer;

    @Bean
    public Step processJsonStep() {
        return new StepBuilder("processJsonStep", jobRepository)
                //Input = SpectatorEntryDto, Output = Map<String, Object>
                .<SpectatorEntryDto, Map<String, Object>>chunk(10, transactionManager)
                .reader(jsonReader.jsonReader())
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipLimit(100)
                .skip(Exception.class)
                .build();
    }

    @Bean
    public Step processXmlStep() {
        return new StepBuilder("processXmlStep", jobRepository)
                //Input = SpectatorEntryDto, Output = Map<String, Object>
                .<SpectatorEntryDto, Map<String, Object>>chunk(10, transactionManager)
                .reader(xmlReader.xmlReader())
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipLimit(100)
                .skip(Exception.class)
                .build();
    }

    @Bean
    public Job jsonJob() {
        return new JobBuilder("jsonJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(processJsonStep())
                .build();
    }

    @Bean
    public Job xmlJob() {
        return new JobBuilder("xmlJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(processXmlStep())
                .build();
    }

//    @Bean
//    public Job spectatorAnalysisJob() {
//        return new JobBuilder("spectatorAnalysisJob", jobRepository)
//                .incrementer(new RunIdIncrementer())
//                .start(processJsonStep())
//                .next(processXmlStep())
//                .build();
//    }
}