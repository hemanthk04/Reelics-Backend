package com.projects.reelics.services;

import com.projects.reelics.models.Track;
import com.projects.reelics.repositories.TrackRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TrackQueryService {

    private final TrackRepo trackRepo;

    public TrackQueryService(TrackRepo trackRepo) {
        this.trackRepo = trackRepo;
    }

    public Optional<Track> findById(UUID id) {
        return trackRepo.findById(id);
    }

    public List<Track> findRecent(int limit) {
        return trackRepo.findTop10ByOrderByCreatedAtDesc();
    }
}
