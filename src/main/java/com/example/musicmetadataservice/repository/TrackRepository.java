package com.example.musicmetadataservice.repository;

import com.example.musicmetadataservice.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findByArtistId(Long artistId);
}
