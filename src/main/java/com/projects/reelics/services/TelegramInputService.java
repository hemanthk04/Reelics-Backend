package com.projects.reelics.services;

import com.projects.reelics.models.Track;
import com.projects.reelics.repositories.TrackRepo;
import com.projects.reelics.utils.InstagramLinkValidator;
import org.springframework.stereotype.Service;

@Service
public class TelegramInputService {
    private final TrackRepo trackRepo;
    private final TrackProcessingService processingService;
    public TelegramInputService(TrackRepo trackRepo, TrackProcessingService processingService){
        this.trackRepo = trackRepo;
        this.processingService = processingService;
    }

    public Track submitInstagramURL(String instagramURL){
        if(!InstagramLinkValidator.isLinkValid(instagramURL)){
            throw new IllegalArgumentException("Invalid Instagram URL is Passed, Retry");
        }

        Track track = new Track();
        track.setInstagramURL(instagramURL);

        Track saved = trackRepo.save(track);

        processingService.processTrackAsync(saved.getId());
        return saved;
    }
}
