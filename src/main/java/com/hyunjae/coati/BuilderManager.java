package com.hyunjae.coati;

import java.util.ArrayList;
import java.util.List;

public class BuilderManager {

    private Builder builder = new Builder();
    private List<Series> seriesList = new ArrayList<>();
    private List<Episode> episodeList = new ArrayList<>();

    public BuilderManager() {
    }
    
    public void build() {
        try {
            List<Series> indexList = builder.buildIndex();
            seriesList.addAll(indexList);

            //List<Series> finaleList = builder.buildFinale();
            //seriesList.addAll(finaleList);

            episodeList = builder.buildEpisode(seriesList);

            builder.updateVideoUrl(episodeList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(builder != null)
                builder.close();
        }
    }

    public List<Series> getSeriesList() {
        return seriesList;
    }

    public List<Episode> getEpisodeList() {
        return episodeList;
    }
}
