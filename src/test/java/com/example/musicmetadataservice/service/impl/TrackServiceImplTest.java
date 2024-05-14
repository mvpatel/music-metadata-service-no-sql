package com.example.musicmetadataservice.service.impl;

import com.example.musicmetadataservice.exception.ResourceNotFoundException;
import com.example.musicmetadataservice.model.Track;
import com.example.musicmetadataservice.repository.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 class TrackServiceImplTest {

    @Mock
    private TrackRepository trackRepository;

    @InjectMocks
    private TrackServiceImpl trackService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddTrack() {
        Track track = new Track();
        when(trackRepository.save(track)).thenReturn(track);

        Track savedTrack = trackService.addTrack(track);

        assertNotNull(savedTrack);
        assertEquals(track, savedTrack);
        verify(trackRepository, times(1)).save(track);
    }

    @Test
    void testFindTracksByArtistIdWhenTracksExist() {
        Long artistId = 1L;
        List<Track> tracks = new ArrayList<>();
        tracks.add(new Track());
        when(trackRepository.findByArtistId(artistId)).thenReturn(tracks);

        List<Track> foundTracks = trackService.findTracksByArtistId(artistId);

        assertNotNull(foundTracks);
        assertEquals(tracks, foundTracks);
        verify(trackRepository, times(1)).findByArtistId(artistId);
    }

    @Test
    void testFindTracksByArtistIdWhenNoTracksExist() {
        Long artistId = 1L;
        when(trackRepository.findByArtistId(artistId)).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> {
            trackService.findTracksByArtistId(artistId);
        });

        verify(trackRepository, times(1)).findByArtistId(artistId);
    }
}
