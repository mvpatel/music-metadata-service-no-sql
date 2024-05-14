package com.example.musicmetadataservice.service.impl;

import com.example.musicmetadataservice.exception.NoArtistAvailableException;
import com.example.musicmetadataservice.exception.ResourceNotFoundException;
import com.example.musicmetadataservice.model.Artist;
import com.example.musicmetadataservice.repository.ArtistRepository;
import com.example.musicmetadataservice.service.ArtistService;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService {

    private static final String ARTIST_NOT_FOUND_MESSAGE = "Artist not found with id ";
    private final ArtistRepository artistRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public Artist addArtist(Artist artist) {
        try {
            return artistRepository.save(artist);
        } catch (DataIntegrityViolationException | EntityExistsException exception) {
            throw new IllegalArgumentException("An artist with the same name already exists");
        }
    }

    @Override
    public Artist updateArtist(Long id, Artist updatedArtist) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ARTIST_NOT_FOUND_MESSAGE + id));

        String newName = updatedArtist.getName();
        if (newName != null && !newName.equals(artist.getName())) {
            if (artistRepository.existsByName(newName)) {
                throw new IllegalArgumentException("An artist with the name '" + newName + "' already exists");
            }
            artist.setName(newName);
        }
        return artistRepository.save(updatedArtist);
    }


    @Override
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    @Override
    public Artist getArtistOfTheDay() {
        List<Artist> allArtists = artistRepository.findAll();
        if (allArtists.isEmpty()) {
            throw new NoArtistAvailableException("No artist is available for the day");
        }
        return allArtists.get((int) (geDayDifferentForArtistOfTheDay() % allArtists.size()));
    }

    public long geDayDifferentForArtistOfTheDay() {
        LocalDate startDate = LocalDate.of(2024, 5, 10);
        return ChronoUnit.DAYS.between(startDate, LocalDate.now());
    }

    @Override
    public Artist getArtistById(Long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ARTIST_NOT_FOUND_MESSAGE + id));
    }
}
