package co.com.nequi.dynamodb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class UserEntity {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;

    @DynamoDbPartitionKey
    public Long getId() {
        return id;
    }
}
