package customer.functions;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import customer.dto.CreateCustomerDto;
import customer.CustomerService;
import customer.entity.CustomerEntity;

/**
 * Handler for requests to Lambda function.
 */
public class CreateCustomer implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        CustomerEntity customer;
        CreateCustomerDto createCustomerDto = null;

        try {
            createCustomerDto = new ObjectMapper().readValue(input.getBody(), CreateCustomerDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return response
                    .withStatusCode(500);
        }

        customer = new CustomerService().create(createCustomerDto);
        String customerJson = "";
        try {
            customerJson = new ObjectMapper().writeValueAsString(customer);
        } catch (IOException e) {
            e.printStackTrace();
            return response
                    .withStatusCode(500);
        }

        return response
                .withStatusCode(201)
                .withBody(customerJson);
    }
}
