package co.com.nequi.api;

import co.com.nequi.model.user.User;
import co.com.nequi.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HandlerTest {

    @Mock
    private UserUseCase userUseCase;

    @InjectMocks
    private Handler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListenCreateUser_Success() {
        Long userId = 123L;
        User user = new User(userId, "johndoe@email.com", "John", "Doe", "testavatarurl");
        ServerRequest request = MockServerRequest.builder()
                .pathVariable("id", String.valueOf(userId))
                .build();

        when(userUseCase.createUser(userId)).thenReturn(Mono.just(user));

        Mono<ServerResponse> responseMono = handler.listenCreateUser(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();

        verify(userUseCase).createUser(userId);
    }

    @Test
    void testListenCreateUser_InvalidId() {
        ServerRequest request = MockServerRequest.builder()
                .pathVariable("id", "invalid")
                .build();

        Mono<ServerResponse> responseMono = handler.listenCreateUser(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();

        verifyNoInteractions(userUseCase);
    }

    @Test
    void testListenFindUser_Success() {
        Long userId = 123L;
        User user = new User(userId, "johndoe@email.com", "John", "Doe", "testavatarurl");
        ServerRequest request = MockServerRequest.builder()
                .pathVariable("id", String.valueOf(userId))
                .build();

        when(userUseCase.findUserById(userId)).thenReturn(Mono.just(user));

        Mono<ServerResponse> responseMono = handler.listenFindUser(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();

        verify(userUseCase).findUserById(userId);
    }

    @Test
    void testListenListUsers_Success() {
        List<User> users = List.of(new User(2L, "johndoe@email.com", "John", "Doe", "testavatarurl"), new User(3L, "johndoe@email.com", "John", "Doe", "testavatarurl"));
        ServerRequest request = MockServerRequest.builder().build();

        when(userUseCase.findAllUsers()).thenReturn(Flux.fromIterable(users));

        Mono<ServerResponse> responseMono = handler.listenListUsers(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();

        verify(userUseCase).findAllUsers();
    }

    @Test
    void testListenFilterUser_Success() {
        String firstName = "John";
        List<User> users = List.of(new User(2L, "johndoe@email.com", "John", "Doe", "testavatarurl"));
        ServerRequest request = MockServerRequest.builder()
                .queryParam("firstName", firstName)
                .build();

        when(userUseCase.findUsersByFirstName(firstName)).thenReturn(Flux.fromIterable(users));

        Mono<ServerResponse> responseMono = handler.listenFilterUser(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();

        verify(userUseCase).findUsersByFirstName(firstName);
    }

    @Test
    void testListenFilterUser_NoResults() {
        String firstName = "NonExistent";
        ServerRequest request = MockServerRequest.builder()
                .queryParam("firstName", firstName)
                .build();

        when(userUseCase.findUsersByFirstName(firstName)).thenReturn(Flux.empty());

        Mono<ServerResponse> responseMono = handler.listenFilterUser(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.NO_CONTENT))
                .verifyComplete();

        verify(userUseCase).findUsersByFirstName(firstName);
    }
}
