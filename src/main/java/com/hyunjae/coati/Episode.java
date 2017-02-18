package com.hyunjae.coati;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Episode {

    @Id
    private int id;
    @Column
    private String title;
    @Column
    private String thumbnailUrl;
    @Column(length = 1000)
    private String videoUrl;
    @Column
    private int timestamp;
    @Column
    private int seriesId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Episode episode = (Episode) o;

        if (id != episode.id) return false;
        if (timestamp != episode.timestamp) return false;
        if (seriesId != episode.seriesId) return false;
        if (title != null ? !title.equals(episode.title) : episode.title != null) return false;
        if (thumbnailUrl != null ? !thumbnailUrl.equals(episode.thumbnailUrl) : episode.thumbnailUrl != null)
            return false;
        return videoUrl != null ? videoUrl.equals(episode.videoUrl) : episode.videoUrl == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (thumbnailUrl != null ? thumbnailUrl.hashCode() : 0);
        result = 31 * result + (videoUrl != null ? videoUrl.hashCode() : 0);
        result = 31 * result + timestamp;
        result = 31 * result + seriesId;
        return result;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", timestamp=" + timestamp +
                ", seriesId=" + seriesId +
                '}';
    }
}
