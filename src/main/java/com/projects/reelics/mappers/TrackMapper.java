package com.projects.reelics.mappers;

import com.projects.reelics.dto.TrackResponse;
import com.projects.reelics.models.Track;

public class TrackMapper {
    public static TrackResponse toResponse(Track track){
        return new TrackResponse(track.getId(), track.getInstagramURL(), track.getStatus(), track.getDriveStreamURL(), track.getCreatedAt());
    }
}
