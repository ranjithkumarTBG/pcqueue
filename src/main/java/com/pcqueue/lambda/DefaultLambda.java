package com.pcqueue.lambda;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApiAsync;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApiAsyncClientBuilder;
import com.amazonaws.services.apigatewaymanagementapi.model.PostToConnectionRequest;
import com.amazonaws.services.apigatewaymanagementapi.model.PostToConnectionResult;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketResponse;
import com.google.gson.Gson;
import com.pcqueue.module.ProductDataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DefaultLambda implements RequestHandler<APIGatewayV2WebSocketEvent, APIGatewayV2WebSocketResponse> {
    private final ProductDataStore productDataStore = new ProductDataStore();
    private final AmazonApiGatewayManagementApiAsync apiAsync;
    private final Gson gson = new Gson();
    private static final Logger logger = LoggerFactory.getLogger(DefaultLambda.class);
    public DefaultLambda(){
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(System.getenv("API_GATEWAY_URL"), "us-east-1");
        this.apiAsync = AmazonApiGatewayManagementApiAsyncClientBuilder.standard().withEndpointConfiguration(endpointConfiguration).build();
    }

    @Override
    public APIGatewayV2WebSocketResponse handleRequest(APIGatewayV2WebSocketEvent apiGatewayV2WebSocketEvent, Context context) {
        List<Item> productList = productDataStore.getProductData();
        String connectionId = apiGatewayV2WebSocketEvent.getRequestContext().getConnectionId();
        pushDataToConnection(connectionId, productList);
        APIGatewayV2WebSocketResponse response = new APIGatewayV2WebSocketResponse();
        response.setStatusCode(200);
        return response;
    }

    private void pushDataToConnection(String connectionId, List<Item> products) {
        try {
            logger.info("pushing to connection:" + this.gson.toJson(products));
            PostToConnectionRequest postToConnectionRequest = new PostToConnectionRequest().withConnectionId(connectionId)
                    .withData(StandardCharsets.UTF_8.newEncoder().encode(CharBuffer.wrap(this.gson.toJson(products))));
            PostToConnectionResult postToConnectionResult =apiAsync.postToConnection(postToConnectionRequest);
            logger.info("pushing to connection completed:");
        } catch (Exception ex) {
            logger.error("Error posting to connection :", ex);

        }
    }
}
