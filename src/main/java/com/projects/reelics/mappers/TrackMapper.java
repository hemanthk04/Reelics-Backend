package com.projects.reelics.mappers;

import com.projects.reelics.dto.TrackResponse;
import com.projects.reelics.models.Track;

public class TrackMapper {

    public static TrackResponse toResponse(Track track) {
        return new TrackResponse(
                track.getId(),
                track.getDisplayName(),
                track.getInstagramURL(),
                track.getStatus(),
                track.isFavorite(),
                track.getStorageURL(),
                track.getDurationSeconds(),
                track.getCreatedAt()
        );
    }
}