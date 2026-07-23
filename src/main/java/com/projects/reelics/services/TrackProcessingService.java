package com.projects.reelics.services;

import com.projects.reelics.models.ProcessingStatus;
import com.projects.reelics.models.Track;
import com.projects.reelics.repositories.TrackRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.projects.reelics.config.RetryPolicy;

import java.io.File;
import java.time.Instant;
import java.util.UUID;

@Service
public class TrackProcessingService {

    private static final Logger log = LoggerFactory.getLogger(TrackProcessingService.class);

    private final TrackRepo trackRepo;
    private final DownloadAudioService downloadAudioService;
    private final R2StorageService r2StorageService;

    public Track updateDisplayName(UUID id, String displayName) {
        Track track = trackRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Track not found"));

        track.setDisplayName(displayName);

        return trackRepo.save(track);
    }

    public Track updateFavorite(UUID id, boolean favorite) {
        Track track = trackRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Track not found"));

        track.setFavorite(favorite);

        return trackRepo.save(track);
    }

    public void deleteTrack(UUID id) {
        Track track = trackRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Track not found"));

        r2StorageService.deleteObject(track.getObjectKey());

        trackRepo.delete(track);
    }

    public TrackProcessingService(
            TrackRepo trackRepo,
            DownloadAudioService downloadAudioService,
            R2StorageService r2StorageService
    ) {
        this.trackRepo = trackRepo;
        this.downloadAudioService = downloadAudioService;
        this.r2StorageService = r2StorageService;
    }

    @Async
    public void processTrackAsync(UUID trackId) {

        Track track = trackRepo.findById(trackId)
                .orElseThrow(() -> new IllegalStateException("Track not found"));

        if (track.getStatus() == ProcessingStatus.COMPLETE ||
                track.getStatus() == ProcessingStatus.DEAD) {
            return;
        }

        // 🚫 Retry limit reached
        if (track.getRetryCount() >= RetryPolicy.MAX_RETRIES) {
            track.setStatus(ProcessingStatus.DEAD);
            trackRepo.save(track);
            return;
        }

        File audioFile = null;

        try {
            track.setStatus(ProcessingStatus.PROCESSING);
            track.setLastTriedAt(Instant.now());
            trackRepo.save(track);

            audioFile = downloadAudioService.downloadAudio(
                    track.getInstagramURL(),
                    track.getId()
            );

            String objectKey = track.getId() + ".mp3";

            String streamUrl = r2StorageService.upload(
                    audioFile,
                    objectKey
            );

            track.setObjectKey(objectKey);
            track.setFileName(audioFile.getName());
            track.setStorageURL(streamUrl);
            track.setStatus(ProcessingStatus.COMPLETE);

            trackRepo.save(track);

        } catch (Exception e) {
            track.setRetryCount(track.getRetryCount() + 1);
            track.setStatus(ProcessingStatus.FAILED);
            track.setErrorMessage(e.getMessage());
            trackRepo.save(track);

        } finally {
            if (audioFile != null && audioFile.exists()) {
                audioFile.delete();
            }
        }
    }

}
