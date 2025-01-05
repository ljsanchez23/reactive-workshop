package co.com.nequi.consumer;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

class RestConsumerTest {

    private static RestConsumer restConsumer;

    private static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        var webClient = WebClient.builder().baseUrl(mockBackEnd.url("/").toString()).build();
        restConsumer = new RestConsumer(webClient);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    @DisplayName("Validar la función findById.")
    void validateFindById() {
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

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
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
