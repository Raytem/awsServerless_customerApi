package customer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import customer.functions.CreateCustomer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCustomerDto {
    private String name;
    private String email;
}
