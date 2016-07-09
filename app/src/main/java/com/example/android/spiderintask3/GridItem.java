package com.example.android.spiderintask3;

/**
 * Created by Srikanth on 7/7/2016.
 */
public class GridItem {
    private String image;
    private String title;
    private String genre;

    public GridItem() {
        super();
    }

    public String getImagePath() {
        return image;
    }

    public void setImagePath(String image) {
        this.image = image;
    }

    public void setMovieTitle(String title){
        this.title = title;
    }

    public String getMovieTitle(){
        return title;
    }

    public void setMovieGenre(String genre){
        this.genre = genre;
    }

    public String getMovieGenre(){
        return genre;
    }

}
