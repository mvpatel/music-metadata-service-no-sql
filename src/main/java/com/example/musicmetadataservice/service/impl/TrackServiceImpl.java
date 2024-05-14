package com.example.musicmetadataservice.service.impl;

import com.example.musicmetadataservice.exception.ResourceNotFoundException;
import com.example.musicmetadataservice.model.Track;
import com.example.musicmetadataservice.repository.TrackRepository;
import com.example.musicmetadataservice.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;

    public TrackServiceImpl(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Override
    public Track addTrack(Track track) {
        return trackRepository.save(track);
    }

    @Override
    public List<Track> findTracksByArtistId(Long artistId) {
        List<Track> trackOptional = trackRepository.findByArtistId(artistId);
        if (trackOptional.isEmpty()) {
            throw new ResourceNotFoundException("Track not found with Artist ID: " + artistId);
        }
        return trackOptional;
    }
}
