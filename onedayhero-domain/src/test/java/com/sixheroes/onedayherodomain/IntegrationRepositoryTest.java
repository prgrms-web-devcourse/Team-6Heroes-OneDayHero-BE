package com.sixheroes.onedayherodomain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationRepositoryTest {

    @Test
    void failTest() {
        assertThat(2 + 3).isEqualTo(2);
    }
}
