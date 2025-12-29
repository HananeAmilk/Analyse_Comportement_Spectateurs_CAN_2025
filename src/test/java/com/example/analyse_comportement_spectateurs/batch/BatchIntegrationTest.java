package com.example.analyse_comportement_spectateurs.batch;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.batch.job.enabled=false",
        "batch.input.json.path=classpath:data/spectators.json",
        "batch.input.xml.path=classpath:data/spectators.xml"
})
class BatchIntegrationTest {

    @Autowired
    private JobLauncher jobLauncher;

//    @Autowired
//    private Job spectatorAnalysisJob;
    @Autowired
    @Qualifier("jsonJob")
    private Job jsonJob;


    @Test
    void testJsonJobExecution() throws Exception {

        JobExecution jobExecution = jobLauncher.run(
                jsonJob,
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters()
        );

        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }



//    @Test
//    void testBatchJobExecution() throws Exception {
//
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addLong("time", System.currentTimeMillis()) // param unique
//                .toJobParameters();
//
//        JobExecution jobExecution = jobLauncher.run(
//                spectatorAnalysisJob,
//                jobParameters
//        );
//
//        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
//        assertTrue(jobExecution.getAllFailureExceptions().isEmpty());
//    }

}