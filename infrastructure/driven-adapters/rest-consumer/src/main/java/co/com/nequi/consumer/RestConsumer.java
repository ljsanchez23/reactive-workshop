package co.com.nequi.consumer;

import co.com.nequi.model.user.User;
import co.com.nequi.model.user.gateways.ExternalUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements ExternalUserRepository {

    private final WebClient webClient;

    @Override
    public Mono<User> findById(Long id) {
        return webClient
                .get()
                .uri("api/users/{id}", id)
                .retrieve()
                .bodyToMono(ReqResResponse.class)
                .map(ReqResResponse::getData)
                .map(data -> {
                    return User.builder()
                            .id((long) data.getId())
                            .email(data.getEmail())
                            .firstName(data.getFirstName())
                            .lastName(data.getLastName())
                            .avatarUrl(data.getAvatar())
                            .build();
                });
    }
}
