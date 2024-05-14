package com.example.musicmetadataservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistDTO {
    private Long id;

    @NotBlank(message = "Name cannot be null or blank")
    private String name;

    @NotEmpty(message = "Aliases cannot be null or blank")
    private Set<String> aliases;
}
