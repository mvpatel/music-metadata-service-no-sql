package com.example.musicmetadataservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "artists")

public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Singular
    @ElementCollection
    @CollectionTable(name = "artist_aliases", joinColumns = @JoinColumn(name = "artist_id"))
    @Column(name = "alias")
    private Set<String> aliases;
}
