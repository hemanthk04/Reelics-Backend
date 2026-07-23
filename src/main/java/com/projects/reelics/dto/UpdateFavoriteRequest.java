package com.projects.reelics.dto;

public class UpdateFavoriteRequest {

    private boolean favorite;

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}