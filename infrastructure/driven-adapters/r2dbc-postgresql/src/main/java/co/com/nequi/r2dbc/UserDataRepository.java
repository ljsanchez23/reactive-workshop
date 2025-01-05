package co.com.nequi.r2dbc;

import co.com.nequi.r2dbc.helper.Constants;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserDataRepository extends ReactiveCrudRepository<UserData, Long>, ReactiveQueryByExampleExecutor<UserData> {
    @Query(Constants.INSERT_USER_QUERY)
    Mono<UserData> insertUser(@Param("user") UserData user);
    Flux<UserData> findByFirstName(String firstName);
}
