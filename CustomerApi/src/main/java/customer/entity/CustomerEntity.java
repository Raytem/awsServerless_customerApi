package customer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamoDbBean
public class CustomerEntity {

    private String id;
    private String name;
    private String email;
    private Instant registrationDate;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + ", email=" + email
                + ", registrationDate=" + registrationDate + "]";
    }

}

