package secretManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.util.Optional;

public class SecretManager {
    public static Optional<AwsAccountCredo> getSecret(String secretName, Region region) {
        SecretsManagerClient secretsManagerClient = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest secretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
        try {
            GetSecretValueResponse  secretValueResponse = secretsManagerClient.getSecretValue(secretValueRequest);
            String secretsJson = secretValueResponse.secretString();

            AwsAccountCredo awsAccountCredo = new ObjectMapper().readValue(secretsJson, AwsAccountCredo.class);

            return Optional.ofNullable(awsAccountCredo);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
