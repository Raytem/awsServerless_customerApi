package secretManager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AwsAccountCredo {
    public String accessKeyId;
    public String SecretAccessKeyId;
}
