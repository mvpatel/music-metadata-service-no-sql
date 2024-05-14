package com.example.musicmetadataservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrackTests {

    @Test
    void testTrackCreationWithBuilder() {
        Artist artist = Artist.builder()
                .name("John Doe")
                .build();

        Track track = Track.builder()
                .id(1L)
                .title("Love Yourself")
                .genre("Pop")
                .length(215)
                .artist(artist)
                .build();

        assertEquals("Love Yourself", track.getTitle());
        assertEquals("Pop", track.getGenre());
        assertEquals(215, track.getLength());
        assertNotNull(track.getArtist());
    }
}
