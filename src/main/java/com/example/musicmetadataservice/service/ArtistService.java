package com.example.musicmetadataservice.service;

import com.example.musicmetadataservice.model.Artist;

import java.util.List;

public interface ArtistService {
    Artist addArtist(Artist artist);

    Artist updateArtist(Long id, Artist updatedArtist);

    List<Artist> getAllArtists();

    Artist getArtistOfTheDay();

    Artist getArtistById(Long id);
}
