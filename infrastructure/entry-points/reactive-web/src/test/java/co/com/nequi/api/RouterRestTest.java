package co.com.nequi.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
@Import({RouterRest.class, Handler.class})
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testRouterRoutes() {
        webTestClient.get()
                .uri("/api/users/filter?firstName=John")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);

        webTestClient.get()
                .uri("/api/users/find/1")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);

        webTestClient.post()
                .uri("/api/users/create/1")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);

        webTestClient.get()
                .uri("/api/users/list")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);
    }
}
