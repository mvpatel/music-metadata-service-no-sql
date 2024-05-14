package com.example.musicmetadataservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackDTO {

    private Long id;

    @NotBlank(message = "Title cannot be empty or blank")
    private String title;

    @NotBlank(message = "Genre cannot be empty or blank")
    private String genre;

    @NotNull(message = "Length must be provided")
    @Min(value = 1, message = "Length must be at least 1 second")
    private Integer length;

    @NotNull(message = "Artist Id must be provided")
    private Long artistId;
}
