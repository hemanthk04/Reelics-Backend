package com.projects.reelics.dto;

import com.projects.reelics.models.ProcessingStatus;

import java.time.Instant;
import java.util.UUID;

public record TrackResponse(UUID id,
                            String instagramURL,
                            ProcessingStatus status,
                            String streamUrl,
                            Instant createdAt) {}
