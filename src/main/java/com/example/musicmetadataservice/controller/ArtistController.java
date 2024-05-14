package com.example.musicmetadataservice.controller;

import com.example.musicmetadataservice.dto.ArtistDTO;
import com.example.musicmetadataservice.model.Artist;
import com.example.musicmetadataservice.service.ArtistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
@Validated
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @PostMapping
    public ResponseEntity<Object> addArtist(@Valid @RequestBody ArtistDTO artistDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToArtistDTO(artistService.addArtist(mapToArtist(artistDTO))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistDTO> updateArtist(@PathVariable Long id, @Valid @RequestBody ArtistDTO artistDTO) {
        return ResponseEntity.ok(mapToArtistDTO(artistService.updateArtist(id, mapToArtist(artistDTO))));
    }

    @GetMapping
    public ResponseEntity<List<ArtistDTO>> getAllArtists() {
        List<ArtistDTO> artistDTOs = artistService.getAllArtists().stream()
                .map(this::mapToArtistDTO)
                .toList();
        return ResponseEntity.ok(artistDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDTO> getArtistById(@PathVariable Long id) {
        return ResponseEntity.ok(mapToArtistDTO(artistService.getArtistById(id)));
    }

    @GetMapping("/artist-of-the-day")
    public ResponseEntity<ArtistDTO> getArtistOfTheDay() {
        return ResponseEntity.ok(mapToArtistDTO(artistService.getArtistOfTheDay()));
    }

    private Artist mapToArtist(ArtistDTO artistDTO) {
        return Artist.builder()
                .id(artistDTO.getId())
                .name(artistDTO.getName())
                .aliases(artistDTO.getAliases())
                .build();
    }

    private ArtistDTO mapToArtistDTO(Artist artist) {
        return ArtistDTO.builder()
                .id(artist.getId())
                .name(artist.getName())
                .aliases(artist.getAliases())
                .build();
    }
}
