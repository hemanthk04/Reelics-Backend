package com.projects.reelics.dto;

import com.projects.reelics.models.ProcessingStatus;

import java.time.Instant;
import java.util.UUID;

public record TrackResponse(UUID id,
                            String displayName,
                            String instagramURL,
                            ProcessingStatus status,
                            boolean favorite,
                            String streamUrl,
                            Integer durationInSeconds,
                            Instant createdAt) {}
