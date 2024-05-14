package com.example.musicmetadataservice.service;

import com.example.musicmetadataservice.model.Track;

import java.util.List;

public interface TrackService {
    Track addTrack(Track track);

    List<Track> findTracksByArtistId(Long artistId);
}
