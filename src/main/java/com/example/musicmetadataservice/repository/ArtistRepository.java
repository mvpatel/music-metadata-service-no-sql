package com.example.musicmetadataservice.repository;

import com.example.musicmetadataservice.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    boolean existsByName(String newName);
}