package com.projects.reelics.repositories;

import com.projects.reelics.models.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TrackRepo extends JpaRepository<Track, UUID> {

    List<Track> findTop10ByOrderByCreatedAtDesc();
}
