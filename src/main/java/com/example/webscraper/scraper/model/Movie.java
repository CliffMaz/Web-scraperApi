package com.example.webscraper.scraper.model;

public class Movie {


    private String title;
    private String type;
    private String rating;
    private String cast;
    private String outline;

    public Movie(String title, String type, String rating, String cast, String outline) {
        this.title = title;
        this.type = type;
        this.rating = rating;
        this.cast = cast;
        this.outline = outline;
    }



    public String getTitle() {
        return title;
    }

    public String getType(){
        return type;
    }

    public String getRating() {
        return rating;
    }

    public String getCast() {
        return cast;
    }

    public String getOutline() {
        return outline;
    }
}

