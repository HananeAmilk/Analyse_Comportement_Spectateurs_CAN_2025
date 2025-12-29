package com.example.analyse_comportement_spectateurs.batch.listener;

import com.example.analyse_comportement_spectateurs.service.StatisticService;
import org.springframework.batch.core.*;
        import org.springframework.stereotype.Component;

@Component
public class StatisticJobListener implements JobExecutionListener {

    private final StatisticService statisticService;

    public StatisticJobListener(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            statisticService.calculateAllStatistics();
        }
    }
}
