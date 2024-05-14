package com.example.musicmetadataservice.controller.integration;

import com.example.musicmetadataservice.dto.ArtistDTO;
import com.example.musicmetadataservice.model.Artist;
import com.example.musicmetadataservice.repository.ArtistRepository;
import com.example.musicmetadataservice.repository.TrackRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArtistControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    TrackRepository trackRepository;

    @BeforeEach
    public void cleanup() {
        trackRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    void testAddArtist() throws Exception {
        ArtistDTO artistDTO = ArtistDTO.builder()
                .name("Test Artist")
                .aliases(Collections.singleton("Test Alias"))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Artist"))
                .andExpect(jsonPath("$.aliases[0]").value("Test Alias"));
    }

    @Test
    void testAddArtist_withSameName_ShouldThrowError() throws Exception {
        ArtistDTO artistDTO = ArtistDTO.builder()
                .name("Test Artist")
                .aliases(Collections.singleton("Test Alias"))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isCreated());

        ArtistDTO artistDTOSameName = ArtistDTO.builder()
                .name("Test Artist")
                .aliases(Collections.singleton("Test Alias"))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTOSameName)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("An artist with the same name already exists"));
    }

    @Test
    void testUpdateArtist() throws Exception {

        ArtistDTO artistDTO = ArtistDTO.builder()
                .name("Original Artist Name")
                .aliases(Collections.singleton("Original Alias"))
                .build();

        ResultActions artistResultActions = mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isCreated());

        Long artistId = objectMapper.readTree(artistResultActions.andReturn().getResponse().getContentAsString()).get("id").asLong();

        Artist updatedArtist = Artist.builder()
                .id(artistId)
                .name("Updated Artist Name")
                .aliases(Collections.singleton("Updated Alias"))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/artists/{id}", artistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedArtist)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Artist Name"))
                .andExpect(jsonPath("$.aliases[0]").value("Updated Alias"));
    }
    @Test
    void testUpdateArtist_WhenIdNotMatched_ShouldThrowError() throws Exception {

        ArtistDTO artistDTO = ArtistDTO.builder()
                .name("Original Artist Name")
                .aliases(Collections.singleton("Original Alias"))
                .build();

        ResultActions artistResultActions = mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isCreated());

        Long artistId = objectMapper.readTree(artistResultActions.andReturn().getResponse().getContentAsString()).get("id").asLong();

        Artist updatedArtist = Artist.builder()
                .id(111L)
                .name("Updated Artist Name")
                .aliases(Collections.singleton("Updated Alias"))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/artists/{id}", artistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedArtist)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An Internal Server Error occurred while processing your request."));
    }

    @Test
    void testAddArtist_WhenNameIsNull_ShouldThrowError() throws Exception {

        ArtistDTO artistDTO = ArtistDTO.builder()
                .aliases(Collections.singleton("Test Alias"))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Name cannot be null or blank"));
    }

    @Test
    void testAddArtist_WhenAliasIsNull_ShouldThrowError() throws Exception {

        ArtistDTO artistDTO = ArtistDTO.builder()
                .name("Test Artist")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Aliases cannot be null or blank"));
    }

    @Test
    void testGetAllArtists() throws Exception {

        ArtistDTO artistDTOOne = ArtistDTO.builder()
                .name("Test Artist 11")
                .aliases(Collections.singleton("Test Alias 11"))
                .build();

        ArtistDTO artistDTOTwo = ArtistDTO.builder()
                .name("Test Artist 12")
                .aliases(Collections.singleton("Test Alias 12"))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTOOne)))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTOTwo)))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.get("/artists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Artist 11"))
                .andExpect(jsonPath("$[0].aliases[0]").value("Test Alias 11"))
                .andExpect(jsonPath("$[1].name").value("Test Artist 12"))
                .andExpect(jsonPath("$[1].aliases[0]").value("Test Alias 12"));
    }
    @Test
    void testGetArtistOfTheDay() throws Exception {

        ArtistDTO artistDTO = ArtistDTO.builder()
                .name("Test Artist of The Day 1")
                .aliases(Collections.singleton("Test Alias of The Day 1"))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.get("/artists/artist-of-the-day")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Artist of The Day 1"))
                .andExpect(jsonPath("$.aliases[0]").value("Test Alias of The Day 1"));
    }

    @Test
    void testGetArtistByID() throws Exception {

        ArtistDTO artistDTO = ArtistDTO.builder()
                .name("Test Artist 41")
                .aliases(Collections.singleton("Test Alias 41"))
                .build();

        ResultActions artistResultActions = mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isCreated());

        Long artistId = objectMapper.readTree(artistResultActions.andReturn().getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(MockMvcRequestBuilders.get("/artists/" + artistId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Artist 41"))
                .andExpect(jsonPath("$.aliases[0]").value("Test Alias 41"));
    }

    @Test
    void testGetArtistByID_WhenArtistNotAvailable_ShouldThrowError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/artists/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Artist not found with id 100"));
    }
}
