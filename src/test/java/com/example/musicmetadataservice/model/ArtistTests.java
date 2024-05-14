package com.example.musicmetadataservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArtistTests {

    @Test
    void testArtistCreationWithBuilder() {
        Artist artist = Artist.builder()
                .id(1L)
                .name("John Doe")
                .alias("JD")
                .build();

        assertEquals("John Doe", artist.getName());
        assertTrue(artist.getAliases().contains("JD"));
    }
}
