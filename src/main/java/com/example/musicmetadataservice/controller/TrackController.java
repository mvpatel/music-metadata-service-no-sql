package com.example.musicmetadataservice.controller;

import com.example.musicmetadataservice.dto.TrackDTO;
import com.example.musicmetadataservice.model.Track;
import com.example.musicmetadataservice.service.ArtistService;
import com.example.musicmetadataservice.service.TrackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TrackService trackService;
    private final ArtistService artistService;

    public TrackController(TrackService trackService, ArtistService artistService) {
        this.trackService = trackService;
        this.artistService = artistService;
    }

    @PostMapping
    public ResponseEntity<TrackDTO> addTrack(@Valid @RequestBody TrackDTO trackDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToTrackDTO(trackService.addTrack(mapToTrack(trackDTO))));
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<TrackDTO>> findTracksByArtistId(@PathVariable Long artistId) {
        List<TrackDTO> tracksDTO = trackService.findTracksByArtistId(artistId)
                .stream()
                .map(this::mapToTrackDTO)
                .toList();
        return ResponseEntity.ok(tracksDTO);
    }

    private Track mapToTrack(TrackDTO trackDTO) {

        return Track.builder()
                .id(trackDTO.getId())
                .title(trackDTO.getTitle())
                .genre(trackDTO.getGenre())
                .length(trackDTO.getLength())
                .artist(artistService.getArtistById(trackDTO.getArtistId()))
                .build();
    }

    private TrackDTO mapToTrackDTO(Track track) {
        return TrackDTO.builder()
                .id(track.getId())
                .title(track.getTitle())
                .genre(track.getGenre())
                .length(track.getLength())
                .artistId(track.getArtist().getId())
                .build();
    }

}
