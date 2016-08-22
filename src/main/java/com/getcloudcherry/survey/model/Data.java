package com.getcloudcherry.survey.model;

/**
 * Created by riteshdubey on 8/21/16.
 */
public class Data {
    public String id;
    public String name;
    public int impression;
    public long duration;
    public long lastViewedAt;

    public Data(String id, int impression, long lastViewedAt) {
        this.id = id;
        this.impression = impression;
        this.lastViewedAt = lastViewedAt;
    }

    public Data(String id, String name, int impression, long lastViewedAt) {
        this.id = id;
        this.name = name;
        this.impression = impression;
        this.lastViewedAt = lastViewedAt;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", impression=" + impression +
                ", duration=" + duration +
                ", lastViewedAt=" + lastViewedAt +
                '}';
    }
}
