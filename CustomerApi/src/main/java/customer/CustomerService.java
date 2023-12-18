package customer;

import customer.dto.CreateCustomerDto;
import customer.dto.UpdateCustomerDto;
import customer.entity.CustomerEntity;
import org.modelmapper.ModelMapper;
import secretManager.AwsAccountCredo;
import secretManager.SecretManager;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class CustomerService {
    static private DynamoDbEnhancedClient ddb;

    static private DynamoDbTable<CustomerEntity> customerTable;

    public CustomerService() throws SecretsManagerException {
        Optional<AwsAccountCredo> awsAccountCredo = SecretManager.getSecret("awsAccountCredo", Region.US_EAST_1);

        if (awsAccountCredo.isPresent()) {
            ddb = DynamoDbEnhancedClient.builder().
                    dynamoDbClient(
                            DynamoDbClient.builder()
                                    .credentialsProvider(StaticCredentialsProvider.create(
                                            AwsBasicCredentials.create(awsAccountCredo.get().getAccessKeyId(), awsAccountCredo.get().getSecretAccessKeyId())
                                    ))
                                    .build()
                    )
                    .build();

            customerTable = ddb.table("Customer", TableSchema.fromBean(CustomerEntity.class));
        } else {
            throw new RuntimeException("Unable to get awsAccountCredo from secretManager");
        }
    }

    public CustomerEntity create(CreateCustomerDto createCustomerDto) {
        CustomerEntity customer = new ModelMapper().map(createCustomerDto, CustomerEntity.class);
        customer.setId(UUID.randomUUID().toString());
        customer.setRegistrationDate(Instant.now());

        try {
            customerTable.putItem(customer);
        } catch (DynamoDbException e) {
            System.err.println(e);
            return null;
        }

        return customer;
    }

    public CustomerEntity getOne(String id) {
        try {
            CustomerEntity customer = customerTable.getItem(
                    Key.builder().partitionValue(id).build()
            );

            return customer;
        } catch (DynamoDbException e) {
            System.err.println(e);
            return null;
        }
    }

    public ArrayList<CustomerEntity> getAll() {
        ArrayList<CustomerEntity> customers = new ArrayList<>();

        PageIterable<CustomerEntity> customersResponse = customerTable.scan();

        for (CustomerEntity customer : customersResponse.items()) {
            customers.add(customer);
        }

        return customers;
    }

    public CustomerEntity update(String id, UpdateCustomerDto updateCustomerDto) {
        CustomerEntity customer = getOne(id);
        if (customer == null) {
            return null;
        }

        CustomerEntity updatedCustomer = CustomerEntityMapper.INSTANCE.updateDtoToEntity(updateCustomerDto, customer);
        CustomerEntity customerFromDb = customerTable.updateItem(updatedCustomer);

        return customerFromDb;
    }

    public CustomerEntity delete(String id) {
        CustomerEntity customer = getOne(id);
        if (customer == null) {
            return null;
        }

        customerTable.deleteItem(Key.builder().partitionValue(id).build());

        return customer;
    }

}
