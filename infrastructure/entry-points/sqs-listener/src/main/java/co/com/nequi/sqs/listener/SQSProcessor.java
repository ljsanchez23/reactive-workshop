package co.com.nequi.sqs.listener;

import co.com.nequi.model.user.User;
import co.com.nequi.usecase.user.UserUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class SQSProcessor implements Function<Message, Mono<Void>> {
    private final UserUseCase userUseCase;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> apply(Message message) {
        log.info("Message Received: {}", message.body());


        try {
            userUseCase.saveOnDynamo(objectMapper.readValue(message.body(), User.class))
                    .subscribe();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return Mono.empty();
    }
}
