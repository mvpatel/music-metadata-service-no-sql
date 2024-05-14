package com.example.musicmetadataservice.service.impl;

import com.example.musicmetadataservice.exception.NoArtistAvailableException;
import com.example.musicmetadataservice.exception.ResourceNotFoundException;
import com.example.musicmetadataservice.model.Artist;
import com.example.musicmetadataservice.repository.ArtistRepository;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArtistServiceImplTest {

    @Mock
    private ArtistRepository artistRepository;

    @Spy
    @InjectMocks
    private ArtistServiceImpl artistService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllArtists() {
        when(artistRepository.findAll()).thenReturn(Collections.singletonList(
                Artist.builder().name("Artist").build()
        ));

        List<Artist> artists = artistService.getAllArtists();

        assertNotNull(artists);
        assertFalse(artists.isEmpty());
        assertEquals("Artist", artists.get(0).getName());
        verify(artistRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testAddArtist() {

        Artist artist = Artist.builder()
                .name("John Doe")
                .build();


        when(artistRepository.save(any(Artist.class))).thenReturn(artist);


        Artist addedArtist = artistService.addArtist(artist);


        assertNotNull(addedArtist);
        assertEquals("John Doe", addedArtist.getName());

        verify(artistRepository, Mockito.times(1)).save(Mockito.any(Artist.class));
    }


    @Test
    void testAddArtist_DuplicateName() {

        Artist existingArtist = Artist.builder()
                .name("Existing Artist")
                .build();


        when(artistRepository.save(any(Artist.class))).thenThrow(EntityExistsException.class);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artistService.addArtist(existingArtist);
        });

        assertEquals("An artist with the same name already exists", exception.getMessage());
    }

    @Test
    void testUpdateArtist() {

        Artist existingArtist = Artist.builder()
                .id(1L)
                .name("Existing Artist")
                .build();


        Artist updatedArtist = Artist.builder()
                .id(1L)
                .name("Updated Artist")
                .build();


        when(artistRepository.findById(1L)).thenReturn(Optional.of(existingArtist));


        when(artistRepository.save(any(Artist.class))).thenReturn(updatedArtist);


        Artist result = artistService.updateArtist(1L, updatedArtist);


        assertNotNull(result);


        assertEquals("Updated Artist", result.getName());

        verify(artistRepository, Mockito.times(1)).findById(1L);
        verify(artistRepository, Mockito.times(1)).save(Mockito.any(Artist.class));
    }

    @Test
    void testUpdateArtist_ArtistExistsByName() {

        
        Long artistId = 1L;
        String existingName = "Existing Artist";
        String updatedName = "Updated Artist";

        Artist existingArtist = new Artist();
        existingArtist.setId(artistId);
        existingArtist.setName(existingName);
        when(artistRepository.findById(artistId)).thenReturn(Optional.of(existingArtist));
        when(artistRepository.existsByName(updatedName)).thenReturn(true);

        Artist updatedArtist = new Artist();
        updatedArtist.setName(updatedName);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> artistService.updateArtist(artistId, updatedArtist));
        String expectedErrorMessage = "An artist with the name '" + updatedName + "' already exists";
        assert (exception.getMessage()).equals(expectedErrorMessage);
    }

    @Test
    void testUpdateArtist_NotFound() {
        Artist updatedArtist = Artist.builder()
                .id(1L)
                .name("Updated Artist")
                .build();

        when(artistRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            artistService.updateArtist(1L, updatedArtist);
        });

        verify(artistRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void testGetArtistById() {
        Artist artist = Artist.builder()
                .id(1L)
                .name("Test Artist")
                .build();

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        Artist result = artistService.getArtistById(1L);

        assertNotNull(result);

        assertEquals("Test Artist", result.getName());
        verify(artistRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void testGetArtistById_NotFound() {
        when(artistRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            artistService.getArtistById(1L);
        });

        verify(artistRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void testSaveDuplicateArtist() {
        Artist artist = Artist.builder()
                .name("John Doe")
                .build();

        when(artistRepository.save(any(Artist.class)))
                .thenThrow(EntityExistsException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            artistService.addArtist(artist);
        });
    }

    @Test
    public void testGetArtistOfTheDayWhenNoArtists() {
        when(artistRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NoArtistAvailableException.class, () -> {
            artistService.getArtistOfTheDay();
        });
    }


    @Test
    void testGetArtistOfTheDayWhenOneArtist() {
        
        Artist artist = new Artist();
        artist.setId(1L);
        artist.setName("Artist 1");
        List<Artist> artists = List.of(artist);
        when(artistRepository.findAll()).thenReturn(artists);

        Artist result = artistService.getArtistOfTheDay();

        assertEquals(artist, result);
    }

    @Test
    void testGetArtistOfTheDayWhenMultipleArtists() {

        List<Artist> artists = new ArrayList<>();
        artists.add(Artist.builder().name("Artist 1").alias("Alias 1").build());
        artists.add(Artist.builder().name("Artist 2").alias("Alias 2").build());
        artists.add(Artist.builder().name("Artist 3").alias("Alias 3").build());
        artists.add(Artist.builder().name("Artist 4").alias("Alias 4").build());
        artists.add(Artist.builder().name("Artist 5").alias("Alias 5").build());
        artists.add(Artist.builder().name("Artist 6").alias("Alias 6").build());
        artists.add(Artist.builder().name("Artist 7").alias("Alias 7").build());
        artists.add(Artist.builder().name("Artist 8").alias("Alias 8").build());
        artists.add(Artist.builder().name("Artist 9").alias("Alias 9").build());
        artists.add(Artist.builder().name("Artist 10").alias("Alias 10").build());

        when(artistService.geDayDifferentForArtistOfTheDay()).thenReturn(2L);
        when(artistRepository.findAll()).thenReturn(artists);

        assertEquals("Artist 3", artistService.getArtistOfTheDay().getName());


        when(artistService.geDayDifferentForArtistOfTheDay()).thenReturn(9L);
        assertEquals("Artist 10", artistService.getArtistOfTheDay().getName());
    }
}
