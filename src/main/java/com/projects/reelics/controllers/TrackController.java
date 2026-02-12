package com.projects.reelics.controllers;

import com.projects.reelics.dto.TrackResponse;
import com.projects.reelics.mappers.TrackMapper;
import com.projects.reelics.models.ProcessingStatus;
import com.projects.reelics.models.Track;
import com.projects.reelics.repositories.TrackRepo;
import com.projects.reelics.services.TrackProcessingService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {

    private final TrackRepo trackRepo;
    private final TrackProcessingService trackProcessingService;

    public TrackController(TrackRepo trackRepo, TrackProcessingService trackProcessingService) {
        this.trackRepo = trackRepo;
        this.trackProcessingService = trackProcessingService;
    }

    @GetMapping
    public List<TrackResponse> getTracks(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        return trackRepo.findAll(
                        PageRequest.of(offset / limit, limit,
                                Sort.by(Sort.Direction.DESC, "createdAt"))
                )
                .stream()
                .map(TrackMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackResponse> getTrack(@PathVariable UUID id) {
        return trackRepo.findById(id)
                .map(track -> ResponseEntity.ok(TrackMapper.toResponse(track)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/retry")
    public ResponseEntity<?> retryTrack(@PathVariable UUID id) {

        Track track = trackRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (track.getStatus() == ProcessingStatus.COMPLETE) {
            return ResponseEntity.badRequest().body("Track already completed");
        }

        if (track.getStatus() == ProcessingStatus.DEAD) {
            return ResponseEntity.badRequest().body("Track is dead");
        }

        trackProcessingService.processTrackAsync(track.getId());
        return ResponseEntity.accepted().build();
    }

}
