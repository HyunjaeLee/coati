package com.hyunjae.coati;

import java.io.IOException;
import java.util.List;

public class BackoffParser extends Parser {

    public BackoffParser() {
        super();
    }

    @Override
    public List<Series> parseIndex() throws IOException {
        return Retry.execute(super::parseIndex);
    }

    @Override
    public int parseFinaleSize() throws IOException {
        return Retry.execute(super::parseFinaleSize);
    }

    @Override
    public List<Series> parseFinale(int page) throws IOException {
        return Retry.execute(() -> super.parseFinale(page));
    }

    @Override
    public List<Episode> parseEpisode(int seriesId, int page) throws IOException {
        return Retry.execute(() -> super.parseEpisode(seriesId, page));
    }

    @Override
    public String parseVideoUrl(int episodeId) throws IOException {
        return Retry.execute(() -> super.parseVideoUrl(episodeId));
    }

    @Override
    public String getRedirectedUrl(String url) throws IOException {
        return Retry.execute(() -> super.getRedirectedUrl(url));
    }
}
