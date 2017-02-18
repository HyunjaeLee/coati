package com.hyunjae.coati;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private EpisodeRepository episodeRepository;

    @Scheduled(fixedRate = 60 * 60 * 1000) // 1 hour
    public void build() {
        log.info("Build started");

        BuilderManager builderManager = new BuilderManager();
        builderManager.build();

        List<Series> seriesList = builderManager.getSeriesList();
        List<Episode> episodeList = builderManager.getEpisodeList();

        seriesList.forEach(series -> {
            if(seriesRepository.exists(series.getId()))
                seriesRepository.delete(series.getId());
            // Finally
            seriesRepository.save(series);
        });

        episodeList.forEach(episode -> {
            if(episodeRepository.exists(episode.getId()))
                episodeRepository.delete(episode.getId());
            // Finally
            episodeRepository.save(episode);
        });

        seriesRepository.flush();
        episodeRepository.flush();

        log.info("Build finished");
    }
}
