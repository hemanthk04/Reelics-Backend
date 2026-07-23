package com.projects.reelics.models;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tracks")
public class Track {
    //Attributes I want in the Table
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false, length = 1000)
    private String instagramURL;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProcessingStatus status;

    @Column(nullable = false)
    private boolean favorite = false;

    //Related to Cloudflare, My Blob storage
    private String objectKey;
    private String fileName;
    private String storageURL;

    //Related to Song, as these are only possible meta-data that is able to and worth collecting
    private Integer durationInSeconds;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @Column(nullable = false)
    private int retryCount = 0;

    @Column
    private Instant lastTriedAt;

    @Column
    private String failureStage; // DOWNLOAD | UPLOAD | UNKNOWN

    //Just in case, If any error message
    @Column(length = 2000)
    private String errorMessage;


    //JPA Hooks
    @PrePersist
    protected void onCreate(){
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.status = ProcessingStatus.PENDING;

        if (this.displayName == null || this.displayName.isBlank()) {
            this.displayName = "Untitled";
        }
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    //Getters and Setters of above-mentioned attributes
    public UUID getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getInstagramURL() {
        return instagramURL;
    }

    public void setInstagramURL(String instagramUrl) {
        this.instagramURL = instagramUrl;
    }

    public ProcessingStatus getStatus() {
        return status;
    }

    public void setStatus(ProcessingStatus status) {
        this.status = status;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStorageURL() {
        return storageURL;
    }

    public void setStorageURL(String storageURL) {
        this.storageURL = storageURL;
    }

    public Integer getDurationSeconds() {
        return durationInSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationInSeconds = durationSeconds;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public Instant getLastTriedAt() {
        return lastTriedAt;
    }

    public void setLastTriedAt(Instant lastTriedAt) {
        this.lastTriedAt = lastTriedAt;
    }

    public String getFailureStage() {
        return failureStage;
    }

    public void setFailureStage(String failureStage) {
        this.failureStage = failureStage;
    }

}
