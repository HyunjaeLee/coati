package com.hyunjae.coati;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Builder {

    private ExecutorService executorService = Executors.newFixedThreadPool(64);
    private Parser parser = new BackoffParser();

    public Builder() {
    }

    public List<Series> buildIndex() throws IOException {
        return parser.parseIndex();
    }

    public List<Series> buildFinale() throws Exception {
        List<Series> finalList = new ArrayList<>();
        List<Future<List<Series>>> futures = new ArrayList<>();

        int page = parser.parseFinaleSize();

        for (int i = 1; i <= page; i++) {
            final int index = i;
            Future<List<Series>> future = executorService.submit(() -> parser.parseFinale(index));
            futures.add(future);
        }

        for (Future<List<Series>> future : futures) {
            List<Series> seriesList = future.get();
            finalList.addAll(seriesList);
        }

        return finalList;
    }

    public List<Episode> buildEpisode(List<Series> seriesList) throws Exception {
        List<Episode> finalList = new ArrayList<>();
        List<Future<List<Episode>>> futures = new ArrayList<>();

        seriesList.forEach(series -> {
            Future<List<Episode>> future = executorService.submit(() ->  buildEpisode(series.getId()));
            futures.add(future);
        });

        for (Future<List<Episode>> future : futures) {
            List<Episode> episodeList = future.get();
            finalList.addAll(episodeList);
        }

        return finalList;
    }

    private List<Episode> buildEpisode(int seriesId) throws IOException {
        List<Episode> finalList = new ArrayList<>();

        int size, page = 1;
        do {
            List<Episode> episodeList = parser.parseEpisode(seriesId, page);
            finalList.addAll(episodeList);
            size = episodeList.size();
            page ++;
        } while (size > 0);

        return finalList;
    }

    public void updateVideoUrl(List<Episode> episodeList) {
        List<Future> futures = new ArrayList<>();

        episodeList.forEach(episode -> {
            Future future = executorService.submit(() -> {
                try {
                    String videoUrl = parser.parseVideoUrl(episode.getId());
                    String redirectedVideoUrl = parser.getRedirectedUrl(videoUrl);
                    episode.setVideoUrl(redirectedVideoUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            );
            futures.add(future);
        });

        futures.forEach(future -> {
            try {
                future.get(); // Blocking
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void close() {
        if(executorService != null)
            executorService.shutdown();
    }
}
