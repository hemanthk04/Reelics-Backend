package com.projects.reelics.dto;

import jakarta.validation.constraints.NotBlank;

public class TelegramSubmitRequest {
    @NotBlank(message = "Instagram URL cant be empty")
    private String instagramURL;

    public String getInstagramURL(){
        return instagramURL;
    }

    public void setInstagramURL(String instagramURL) {
        this.instagramURL = instagramURL;
    }
}
