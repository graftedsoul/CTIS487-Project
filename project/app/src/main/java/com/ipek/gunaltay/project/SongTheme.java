package com.ipek.gunaltay.project;

import java.util.ArrayList;

public class SongTheme {
    private String name;
    private ArrayList<String> songs;

    public SongTheme(String name, ArrayList<String> songs) {
        this.name = name;
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<String> songs) {
        this.songs = songs;
    }
}
