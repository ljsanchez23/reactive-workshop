package co.com.nequi.api;

import co.com.nequi.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {
    private  final UserUseCase userUseCase;

    public Mono<ServerResponse> listenCreateUser(ServerRequest serverRequest) {
        log.info("Iniciando proceso para crear usuario.");
        Long userId;

        try {
            userId = Long.valueOf(serverRequest.pathVariable("id"));
            log.info("Usuario ID recibido: {}", userId);
        } catch (NumberFormatException e) {
            log.error("El ID proporcionado no es válido: {}", serverRequest.pathVariable("id"), e);
            return ServerResponse.badRequest().bodyValue("El ID proporcionado no es válido.");
        }

        return userUseCase.createUser(userId)
                .doOnNext(user -> log.info("Usuario creado o existente: {}", user))
                .flatMap(savedUser -> {
                    log.info("Usuario guardado exitosamente: {}", savedUser);
                    return ServerResponse.ok().bodyValue(savedUser);
                })
                .onErrorResume(e -> {
                    log.error("Error al procesar la solicitud para el usuario ID {}: {}", userId, e.getMessage(), e);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Error al procesar la solicitud: " + e.getMessage());
                });
    }

    public Mono<ServerResponse> listenFindUser(ServerRequest serverRequest) {
        Long userId = Long.valueOf(serverRequest.pathVariable("id"));
        log.info("Iniciando búsqueda de usuario con userId={}", userId);

        return userUseCase.findUserById(userId)
                .doOnNext(user -> log.info("Usuario encontrado: {}", user))
                .flatMap(user -> {
                    log.info("Devolviendo userId={} desde el handler", user.getId());
                    return ServerResponse.ok().bodyValue(user);
                })
                .doOnError(e -> log.error("Error al buscar el userId={}: {}", userId, e))
                .onErrorResume(e -> Mono.error(e));
    }

    public Mono<ServerResponse> listenListUsers(ServerRequest serverRequest) {
        log.info("Iniciando solicitud para encontrar todos los usuarios");

        return userUseCase.findAllUsers()
                .doOnNext(user -> log.info("Usuario encontrado: {}", user))
                .collectList()
                .flatMap(users -> {
                    log.info("Devolviendo {} usuarios", users.size());
                    return ServerResponse.ok().bodyValue(users);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("No se encontraron usuarios");
                    return ServerResponse.noContent().build();
                }))
                .onErrorResume(e -> {
                    log.error("Error al obtener usuarios: {}", e.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Error al consultar usuarios: " + e.getMessage());
                });
    }

    public Mono<ServerResponse> listenFilterUser(ServerRequest serverRequest) {
        String firstName = serverRequest.queryParam("firstName").orElse("");
        log.info("Iniciando solicitud para buscar usuarios con el nombre: {}", firstName);

        return userUseCase.findUsersByFirstName(firstName)
                .doOnNext(user -> log.info("Usuario encontrado con el filtro: {}", user))
                .collectList()
                .flatMap(users -> {
                    if (users.isEmpty()) {
                        log.info("No se encontraron usuarios con el nombre: {}", firstName);
                        return ServerResponse.noContent().build();
                    }
                    log.info("Devolviendo {} usuarios con el nombre: {}", users.size(), firstName);
                    return ServerResponse.ok().bodyValue(users);
                })
                .onErrorResume(e -> {
                    log.error("Error al buscar usuarios con el nombre: {}: {}", firstName, e.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Error al consultar usuarios: " + e.getMessage());
                });
    }
}
