package com.example.mytheaterapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Play implements Serializable {
    private String name;
    private ArrayList<String> cast = new ArrayList<>();
    private String genre;
    private String plot;
    private String imgPath;

    public Play(String name, ArrayList<String> cast, String genre, String plot, String imgPath){
        setCast(cast);
        setGenre(genre);
        setPlot(plot);
        setName(name);
        setImgPath(imgPath);
    }

    public void setCast(ArrayList<String> cast) {
        this.cast = cast;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getGenre() {
        return genre;
    }

    public String getName() {
        return name;
    }

    public String getPlot() {
        return plot;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
