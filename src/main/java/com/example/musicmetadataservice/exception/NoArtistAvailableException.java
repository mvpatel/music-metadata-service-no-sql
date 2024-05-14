package com.example.musicmetadataservice.exception;

public class NoArtistAvailableException extends RuntimeException {
    public NoArtistAvailableException(String message) {
        super(message);
    }
}
