package co.com.nequi.usecase.user;

import co.com.nequi.exception.MalformedFirstNameException;
import co.com.nequi.exception.UnableToListUsersException;
import co.com.nequi.exception.UserDoesNotExistsException;
import co.com.nequi.exception.UserNotFoundInReqresException;
import co.com.nequi.model.user.User;
import co.com.nequi.model.user.gateways.DynamoRepository;
import co.com.nequi.model.user.gateways.ExternalUserRepository;
import co.com.nequi.model.user.gateways.RedisRepository;
import co.com.nequi.model.user.gateways.SQSSenderRepository;
import co.com.nequi.model.user.gateways.UserRepository;
import co.com.nequi.usecase.user.helper.Constants;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final ExternalUserRepository externalUserRepository;
    private final RedisRepository redisRepository;
    private final SQSSenderRepository sqsSenderRepository;
    private final DynamoRepository dynamoRepository;

    public Mono<User> createUser(Long userId) {
        return userRepository.findById(userId)
                .flatMap(Mono::just)
                .switchIfEmpty(
                        externalUserRepository.findById(userId)
                                .flatMap(userFromReqres -> {
                                    if (userFromReqres == null) {
                                        return Mono.error(new UserNotFoundInReqresException(Constants.USER_NOT_FOUND_IN_REQRES_IN));
                                    }
                                    return userRepository.insert(userFromReqres)
                                            .flatMap(savedUser ->
                                                    sqsSenderRepository.sendEvent(savedUser)
                                                            .thenReturn(savedUser)
                                            );
                                })
                )
                .onErrorResume(e -> {
                    return Mono.error(new Exception(e));
                });
    }

    public Mono<User> findUserById(Long userId) {
        return redisRepository.getById(userId)
                .switchIfEmpty(
                        userRepository.findById(userId)
                                .flatMap(userFromDb -> redisRepository.save(userFromDb)
                                        .thenReturn(userFromDb))
                                .switchIfEmpty(Mono.error(new UserDoesNotExistsException(Constants.USER_NOT_FOUND + userId)))
                );
    }

    public Flux<User> findAllUsers() {
        return userRepository.findAll()
                .onErrorResume(e -> Flux.error(new UnableToListUsersException(Constants.COULD_NOT_LOAD_USER_LIST)));
    }

    public Flux<User> findUsersByFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            return Flux.error(new MalformedFirstNameException(Constants.FIRST_NAME_CAN_NOT_BE_EMPTY));
        }

        return userRepository.findByFirstName(firstName)
                .switchIfEmpty(Flux.empty())
                .onErrorResume(e -> Flux.error(new UnableToListUsersException(Constants.COULD_NOT_FILTER_BY_NAME)));
    }

    public Mono<User> saveOnDynamo(User user) {
        if (user == null) {
            return Mono.error(new IllegalArgumentException(Constants.USER_CAN_NOT_BE_NULL));
        }

        User dynamoUser = User
                .builder()
                .id(user.getId())
                .email(user.getEmail().toUpperCase())
                .firstName(user.getFirstName().toUpperCase())
                .lastName(user.getLastName().toUpperCase())
                .avatarUrl(user.getAvatarUrl().toUpperCase())
                .build();

        return dynamoRepository.save(dynamoUser);
    }

}
