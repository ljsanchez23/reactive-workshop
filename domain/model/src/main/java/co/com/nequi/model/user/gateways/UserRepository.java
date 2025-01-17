package co.com.nequi.model.user.gateways;

import co.com.nequi.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> findById(Long id);
    Mono<User> save(User user);
    Flux<User> findAll();
    Flux<User> findByFirstName(String firstName);
    Mono<User> insert(User user);
}
