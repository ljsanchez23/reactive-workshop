package co.com.nequi.model.user.gateways;

import co.com.nequi.model.user.User;
import reactor.core.publisher.Mono;

public interface SQSSenderRepository {
    Mono<String> sendEvent(User user);
}
