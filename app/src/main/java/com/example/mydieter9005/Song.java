package com.example.mydieter9005;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class Song extends AppCompatActivity implements Serializable {
    private static ArrayList<Song> songs = new ArrayList<Song>(); // Contain every song basic info.
    private String name;
    private boolean isPlaying;
    private final int id;

    public Song(String name, int id){
        this.name = name;
        this.isPlaying = false;
        this.id = id;
        songs.add(this);
    }

    public void playSong(){
        setAllSongSilence();
        this.isPlaying = true;
    }

    public void setAllSongSilence(){
        for(int i = 0; i < songs.size(); i++)
            songs.get(i).isPlaying = false;
    }

    public static int getActiveSongIndex(){
        for(int i = 0; i < songs.size(); i++){
            if(songs.get(i).isPlaying)
                return i;
        }
        songs.get(0).playSong();
        return 0;  // Initiated song;
    }

    public static Song getActiveSong(){
        for(int i = 0; i < songs.size(); i++){
            if(songs.get(i).isPlaying)
                return songs.get(i);
        }
        songs.get(0).playSong();
        return songs.get(0);  // Initiated song;
    }

    public static Song getSongByName(String name){
        name = name.replaceAll(" ", "_");
        for(int i = 0; i < songs.size(); i++){
            if(songs.get(i).name.equals(name))
                return songs.get(i);
        }
        return null;
    }

    public static ArrayList<Song> getSongs() {
        return songs;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static void initiateSongs(){
        new Song("happy_clappy_ukulele", R.raw.happy_clappy_ukulele).playSong();
        new Song("best_prank", R.raw.best_time);
        new Song("childish_prank", R.raw.childish_prank);
        new Song("electronic_rock_king_around_here", R.raw.electronic_rock_king_around_here);
        new Song("energetic_hip_hop", R.raw.energetic_hip_hop);
        new Song("freshness", R.raw.freshness);
        new Song("happy_summer", R.raw.happy_summer);
        new Song("jazzy_abstract_beat", R.raw.jazzy_abstract_beat);
        new Song("life_of_a_wondering_wizard", R.raw.life_of_a_wandering_wizard);
        new Song("soft_lofi_beat", R.raw.soft_lofi_beat);
    }

    @Override
    public String toString(){
        return this.name + ": is playing ? - " + this.isPlaying;
    }
}
