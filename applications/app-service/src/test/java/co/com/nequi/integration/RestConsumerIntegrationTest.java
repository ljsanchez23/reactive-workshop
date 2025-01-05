package co.com.nequi.integration;

import co.com.nequi.consumer.RestConsumer;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import reactor.test.StepVerifier;

import java.io.IOException;

@SpringBootTest
class RestConsumerIntegrationTest {

    private static MockWebServer mockWebServer;

    @Autowired
    private RestConsumer restConsumer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void validateFindByIdIntegration() {
        String mockResponseBody = """
                {
                  "data": {
                    "id": 1,
                    "email": "john.doe@example.com",
                    "first_name": "John",
                    "last_name": "Doe",
                    "avatar": "https://example.com/avatar.jpg"
                  }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(mockResponseBody));

        var response = restConsumer.findById(1L);

        StepVerifier.create(response)
                .expectNextMatches(user -> user.getId().equals(1L) &&
                        user.getEmail().equals("john.doe@example.com") &&
                        user.getFirstName().equals("John") &&
                        user.getLastName().equals("Doe") &&
                        user.getAvatarUrl().equals("https://example.com/avatar.jpg"))
                .verifyComplete();
    }
}
