package co.com.nequi.r2dbc;

import co.com.nequi.model.user.User;
import co.com.nequi.model.user.gateways.UserRepository;
import co.com.nequi.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
    User,
        UserData,
    Long,
        UserDataRepository
> implements UserRepository {
    public UserRepositoryAdapter(UserDataRepository repository, ObjectMapper mapper) {

        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Flux<User> findByFirstName(String firstName) {
        return repository.findByFirstName(firstName)
                .map(this::toEntity);
    }

    public Mono<User> insert(User user) {
        UserData data = toData(user);
        return repository.insertUser(data)
                .map(this::toEntity)
                .doOnError(e -> log.error("Error inserting user: {}", e.getMessage()));
    }
}

