package com.example.musicmetadataservice.controller.integration;

import com.example.musicmetadataservice.dto.ArtistDTO;
import com.example.musicmetadataservice.dto.TrackDTO;
import com.example.musicmetadataservice.repository.ArtistRepository;
import com.example.musicmetadataservice.repository.TrackRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
public class TrackControllerIntegrationTest {

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
    void testAddTrack() throws Exception {

        ArtistDTO artistDTO = ArtistDTO.builder()
                .name("Test Artist 1")
                .aliases(Collections.singleton("Alias 1"))
                .build();

        ResultActions artistResultActions = mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isCreated());

        Long artistId = objectMapper.readTree(artistResultActions.andReturn().getResponse().getContentAsString()).get("id").asLong();

        TrackDTO trackDTO = TrackDTO.builder()
                .title("Test Track 1")
                .genre("Pop")
                .length(180)
                .artistId(artistId)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trackDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Track 1"))
                .andExpect(jsonPath("$.genre").value("Pop"))
                .andExpect(jsonPath("$.length").value(180))
                .andExpect(jsonPath("$.artistId").value(artistId));
    }


    @Test
    void testFindTracksByArtistId() throws Exception {

        ArtistDTO artistDTO = ArtistDTO.builder()
                .name("Test Artist 14")
                .aliases(Collections.singleton("Test Alias 14"))
                .build();

        ResultActions artistResultActions = mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isCreated());

        Long artistId = objectMapper.readTree(artistResultActions.andReturn().getResponse().getContentAsString()).get("id").asLong();

        TrackDTO trackDTO1 = TrackDTO.builder()
                .title("Test Track 1")
                .genre("Pop")
                .length(180)
                .artistId(artistId)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trackDTO1)))
                .andExpect(status().isCreated());

        TrackDTO trackDTO2 = TrackDTO.builder()
                .title("Test Track 2")
                .genre("classic")
                .length(140)
                .artistId(artistId)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trackDTO2)))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.get("/tracks/artist/" + artistId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Track 1"))
                .andExpect(jsonPath("$[0].genre").value("Pop"))
                .andExpect(jsonPath("$[0].length").value(180))
                .andExpect(jsonPath("$[0].artistId").value(artistId))
                .andExpect(jsonPath("$[1].title").value("Test Track 2"))
                .andExpect(jsonPath("$[1].genre").value("classic"))
                .andExpect(jsonPath("$[1].length").value(140))
                .andExpect(jsonPath("$[1].artistId").value(artistId));
    }

    @Test
    void testAddTrack_WhenTitleNull_ShouldThrowError() throws Exception {

        ArtistDTO artistDTO = ArtistDTO.builder()
                .name("Test Artist 2")
                .aliases(Collections.singleton("Alias 2"))
                .build();

        ResultActions artistResultActions = mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isCreated());

        Long artistId = objectMapper.readTree(artistResultActions.andReturn().getResponse().getContentAsString()).get("id").asLong();

        TrackDTO trackDTO = TrackDTO.builder()
                .genre("Pop")
                .length(180)
                .artistId(artistId)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trackDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Title cannot be empty or blank"));
    }

    @Test
    void testAddTrack_WhenGenreNull_ShouldThrowError() throws Exception {

        ArtistDTO artistDTO = ArtistDTO.builder()
                .name("Test Artist 3")
                .aliases(Collections.singleton("Alias 3"))
                .build();

        ResultActions artistResultActions = mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isCreated());

        Long artistId = objectMapper.readTree(artistResultActions.andReturn().getResponse().getContentAsString()).get("id").asLong();

        TrackDTO trackDTO = TrackDTO.builder()
                .title("Test Track 1")
                .length(180)
                .artistId(artistId)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trackDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Genre cannot be empty or blank"));
    }

    @Test
    void testAddTrack_WhenLengthNull_ShouldThrowError() throws Exception {

        ArtistDTO artistDTO = ArtistDTO.builder()
                .name("Test Artist 4")
                .aliases(Collections.singleton("Alias 4"))
                .build();

        ResultActions artistResultActions = mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isCreated());

        Long artistId = objectMapper.readTree(artistResultActions.andReturn().getResponse().getContentAsString()).get("id").asLong();

        TrackDTO trackDTO = TrackDTO.builder()
                .title("Test Track 1")
                .genre("Pop")
                .artistId(artistId)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trackDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Length must be provided"));
    }

    @Test
    void testAddTrack_WhenLengthZero_ShouldThrowError() throws Exception {

        ArtistDTO artistDTO = ArtistDTO.builder()
                .name("Test Artist 5")
                .aliases(Collections.singleton("Alias 5"))
                .build();

        ResultActions artistResultActions = mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isCreated());

        Long artistId = objectMapper.readTree(artistResultActions.andReturn().getResponse().getContentAsString()).get("id").asLong();

        TrackDTO trackDTO = TrackDTO.builder()
                .title("Test Track 1")
                .genre("Pop")
                .length(0)
                .artistId(artistId)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trackDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Length must be at least 1 second"));
    }

    @Test
    void testAddTrack_WhenArtistIdNull_ShouldThrowError() throws Exception {
        TrackDTO trackDTO = TrackDTO.builder()
                .title("Test Track 1")
                .genre("Pop")
                .length(6)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trackDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Artist Id must be provided"));
    }
}
