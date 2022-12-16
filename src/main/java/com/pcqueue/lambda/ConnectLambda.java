package com.pcqueue.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketResponse;
import com.pcqueue.module.ProductDataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectLambda implements RequestHandler<APIGatewayV2WebSocketEvent, APIGatewayV2WebSocketResponse> {

    private static final Logger logger = LoggerFactory.getLogger(ConnectLambda.class);
    private final ProductDataStore productDataStore = new ProductDataStore();

    @Override
    public APIGatewayV2WebSocketResponse handleRequest(APIGatewayV2WebSocketEvent apiGatewayV2WebSocketEvent, Context context) {
        String connectionId = null;
        logger.info("WebSocket ConnectionId {}", apiGatewayV2WebSocketEvent.getRequestContext().getConnectionId());
        System.out.println("WebSocket ConnectionId {}" + apiGatewayV2WebSocketEvent.getRequestContext().getConnectionId());
        connectionId = apiGatewayV2WebSocketEvent.getRequestContext().getConnectionId();
        productDataStore.saveConnectionId(connectionId);
        APIGatewayV2WebSocketResponse response = new APIGatewayV2WebSocketResponse();
        response.setStatusCode(200);
        return response;

    }
}
