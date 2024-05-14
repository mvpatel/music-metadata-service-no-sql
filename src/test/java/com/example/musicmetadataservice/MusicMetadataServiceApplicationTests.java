package com.example.musicmetadataservice;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MusicMetadataServiceApplicationTests {

    @Test
    void mainRunsSuccessfully() {
        try (MockedStatic<SpringApplication> mockedSpringApplication = Mockito.mockStatic(SpringApplication.class)) {

            MusicMetadataServiceApplication.main(new String[]{});

            mockedSpringApplication.verify(() -> SpringApplication.run(MusicMetadataServiceApplication.class, new String[]{}));
        }
    }

}
