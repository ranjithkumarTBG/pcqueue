package com.pcqueue.module;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ProductDataStore {

    private static final Logger logger = LoggerFactory.getLogger(ProductDataStore.class);
    private final DynamoDB dynamoDB;

    public ProductDataStore() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        dynamoDB = new DynamoDB(client);
        logger.info("Database connected");
    }

    public void saveConnectionId(String connectionId) {
        try {
            Map<String, AttributeValue> item_value = new HashMap<>();
            item_value.put("connectionId", new AttributeValue(connectionId));
            Item item = new Item().withPrimaryKey("connectionId", connectionId);
            logger.info("Get ConnectionId before saving DynamoDB PCQueueConnections" + item.toString());
            Table table = dynamoDB.getTable("PCQueueConnections");
            table.putItem(item);
            logger.info("Saving completed to PCQueueConnections table ");
        } catch (Exception ex) {
            logger.error("Error saving data to PCQueueConnections table");

        }

    }

    public void deleteConnectionId(String connectionId) {

        try {
            Map<String, AttributeValue> item_value = new HashMap<>();
            item_value.put("connectionId", new AttributeValue(connectionId));
            DeleteItemSpec spec = new DeleteItemSpec().withPrimaryKey("connectionId", connectionId);
            dynamoDB.getTable("PCQueueConnections").deleteItem(spec);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public List<String> getConnectionIds() {
        ScanSpec scanSpec = new ScanSpec().withProjectionExpression("connectionId");
        List<String> list = new ArrayList<>();

        try {
            Table table = dynamoDB.getTable("PCQueueConnections");
            ItemCollection<ScanOutcome> items = table.scan(scanSpec);

			for (Item value : items) {
                String connectionId = (String) value.get("connectionId");
				list.add(connectionId);
			}

        } catch (Exception e) {
            logger.error("Unable to scan the table:{}", e.getMessage());
        }
        logger.info("connection size:" + list.size());
        return list;
    }

    public List<Item> getProductData() {
        ScanSpec scanSpec = new ScanSpec().withProjectionExpression("id, productname, price");
        List<Item> list = new ArrayList<>();
        try {
            Table table = dynamoDB.getTable("Products");
            ItemCollection<ScanOutcome> items = table.scan(scanSpec);

            Iterator<Item> iter = items.iterator();

            while (iter.hasNext()) {
                Item item = iter.next();
                list.add(item);
                System.out.println(item.toString());
            }

        } catch (Exception e) {
			logger.error("Unable to scan the table:{}", e.getMessage());
        }

        logger.info("connection size:" + list.size());
        return list;
    }

}
