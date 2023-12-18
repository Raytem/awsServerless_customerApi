package customer.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import customer.CustomerService;
import customer.entity.CustomerEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetOneCustomer implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        String customerId = input.getPathParameters().get("id");

        if (customerId == null) {
            return response.withStatusCode(400);
        }

        CustomerEntity customer = new CustomerService().getOne(customerId);
        if (customer == null) {
            return response.withStatusCode(404);
        }

        try {
            String customerJson = new ObjectMapper().writeValueAsString(customer);
            return response.withBody(customerJson).withStatusCode(200);
        } catch (IOException e) {
            e.printStackTrace();
            return response.withStatusCode(500);
        }

    }
}
