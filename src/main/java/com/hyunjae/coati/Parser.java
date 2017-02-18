package com.hyunjae.coati;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final String USERAGENT = "Mozilla/5.0";
    private static final Pattern PATTERN = Pattern.compile("var videoID = '(.*?)'");
    private static final String[] DAYS = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    public Parser() {
    }

    private int parseId(String url) { // Returns ID from given URL
        int lastIndex = url.lastIndexOf('-') + 1;
        String idString = url.substring(lastIndex);
        return Integer.parseInt(idString);
    }

    public List<Series> parseIndex() throws IOException {
        Document document = Jsoup.connect("https://anigod.com/")
                .userAgent(USERAGENT)
                .get();

        Elements elements = document.select(".index-table-container");

        List<Series> list = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            final int index = i; // Variable used in lambda should be final or effectively final
            elements.get(index)
                    .select(".index-image-container.badge")
                    .forEach(element -> {
                        int id = parseId(element.attr("href"));
                        String title = element.attr("title");
                        String thumbnailUrl = element.select(".index-image").first().attr("src");
                        int timestamp = Integer.parseInt(element.attr("timestamp"));
                        String day = DAYS[index];

                        Series series = new Series();
                        series.setId(id);
                        series.setTitle(title);
                        series.setThumbnailUrl(thumbnailUrl);
                        series.setTimestamp(timestamp);
                        series.setDay(day);

                        list.add(series);
                    });
        }

        return list;
    }

    public int parseFinaleSize() throws IOException {
        Document document = Jsoup.connect("https://anigod.com/animations/finale/text")
                .userAgent(USERAGENT)
                .get();

        int size = document.select(".table-link").size();

        return (size / 30) + 1; // Each page has 30 elements
    }

    public List<Series> parseFinale(int page) throws IOException {
        String url = "https://anigod.com/animations/finale/title/asc/" + page;

        Document document = Jsoup.connect(url)
                .userAgent(USERAGENT)
                .get();

        Elements elements = document.select(".table-image-container");

        List<Series> list = new ArrayList<>();

        elements.forEach(element -> {
            String title = element.attr("title");
            int id = parseId(element.attr("href"));
            String thumbnailUrl = element.select(".lazy").first().attr("data-original");
            int timestamp = Integer.parseInt(element.attr("timestamp"));

            Series series = new Series(); // Finale does not have 'day' values;
            series.setTitle(title);
            series.setId(id);
            series.setThumbnailUrl(thumbnailUrl);
            series.setTimestamp(timestamp);

            list.add(series);
        });

        return list;
    }

    public List<Episode> parseEpisode(int seriesId, int page) throws IOException {
        String url = "https://anigod.com/animation/" + seriesId + "/" + page;

        Document document = Jsoup.connect(url)
                .userAgent(USERAGENT)
                .get();

        Elements elements = document.select(".table-image-container");

        List<Episode> list = new ArrayList<>();

        elements.forEach(element -> {
            int id = parseId(element.attr("href"));
            String title = element.attr("title");
            String thumbnailUrl = element.select(".lazy").first().attr("data-original");
            int timestamp = Integer.parseInt(element.attr("timestamp"));

            Episode episode = new Episode();
            episode.setId(id);
            episode.setTitle(title);
            episode.setThumbnailUrl(thumbnailUrl);
            episode.setTimestamp(timestamp);
            episode.setSeriesId(seriesId);

            list.add(episode);
        });

        return list;
    }

    public String parseVideoUrl(int episodeId) throws IOException {
        String url = "https://anigod.com/episode/" + episodeId;

        Document document = Jsoup.connect(url)
                .userAgent(USERAGENT)
                .timeout(0)
                .referrer("http://sh.st/")
                .get();

        String html = document.outerHtml();

        String videoId;
        Matcher matcher = PATTERN.matcher(html);
        if(matcher.find())
            videoId = matcher.group(1);
        else
            throw new IOException("Invalid HTML source");

        String escapedVideoId = URLDecoder.decode(videoId, "UTF-8")
                .replace("\\x", "%")
                .replace("\\", "");

        return "https://anigod.com/video?id=" + escapedVideoId + "&ts=" + System.currentTimeMillis();
    }

    public String getRedirectedUrl(String url) throws IOException {
        String redirectUrl = url;

        HttpURLConnection connection = (HttpURLConnection) new URL(redirectUrl).openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("User-Agent", USERAGENT);
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
            String location = connection.getHeaderField("Location");
            if (location != null)
                redirectUrl = location;
        }

        return redirectUrl;
    }
}
