package com.test.commons.shopify.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.commons.shopify.gateway.SendNotificationApiGateway;
import com.test.commons.shopify.model.enums.ShipmentStatusEnum;
import com.test.commons.shopify.model.request.UpdateOrderRequest;
import com.test.commons.shopify.model.shopify.Fulfillment;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.charset.Charset;

public class ShopifyWebhookHandler implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        context.getLogger().log("[Start][ShopifyWebhookHandler][handleRequest]");
        JSONParser parser = new JSONParser();
        JSONObject responseJson = new JSONObject();
        ObjectMapper objectMapper = new ObjectMapper();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("US-ASCII")))){
            JSONObject event = (JSONObject) parser.parse(reader);
            if (event.get("body") != null) {
                Fulfillment fulfillment = objectMapper.readValue((String) event.get("body"), Fulfillment.class);
                if(fulfillment != null && !StringUtils.isEmpty(fulfillment.getShipmentStatus()) ){
                    UpdateOrderRequest request = UpdateOrderRequest.builder()
                            .message("The order is with the shipping status "+ fulfillment.getShipmentStatus())
                            .orderId(fulfillment.getOrderId().toString())
                            .shipmentStatus(fulfillment.getShipmentStatus())
                            .build();
                    responseJson.put("body", objectMapper.writeValueAsString(request));
                    if(ShipmentStatusEnum.DELIVERED.toString().equalsIgnoreCase(fulfillment.getShipmentStatus())
                                || ShipmentStatusEnum.IN_TRANSIT.toString().equalsIgnoreCase(fulfillment.getShipmentStatus())
                                || ShipmentStatusEnum.OUT_FOR_DELIVERY.toString().equalsIgnoreCase(fulfillment.getShipmentStatus())) {

                        callSendNotificationEndpoint(fulfillment);
                    }else{
                        updateShippingStatus(fulfillment);
                    }
                }
            }
        } catch (Exception e) {
            context.getLogger().log("[Error][ShopifyWebhookHandler]Error in handler"+ e.getMessage());
            responseJson.put("statusCode", 400);
        }
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
        context.getLogger().log("[End][ShopifyWebhookHandler][handleRequest]");
    }

    private void callSendNotificationEndpoint(Fulfillment fulfillment) throws Exception{

        getNotificationApiGateway().sendNotification(UpdateOrderRequest.builder()
                        .message("The order is with the shipping status "+ fulfillment.getShipmentStatus())
                        .orderId(fulfillment.getOrderId().toString())
                        .shipmentStatus(fulfillment.getShipmentStatus())
                .build());
    }

    private void updateShippingStatus(Fulfillment fulfillment) throws Exception{
        getNotificationApiGateway().updateShippingStatusOrder(UpdateOrderRequest.builder()
                .orderId(fulfillment.getOrderId().toString())
                .shipmentStatus(fulfillment.getShipmentStatus()).build());
    }

    private SendNotificationApiGateway getNotificationApiGateway(){
        String notificationURL = System.getenv("LIGHTBOX_URL");

        return Feign.builder().client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(SendNotificationApiGateway.class))
                .logLevel(Logger.Level.FULL)
                .target(SendNotificationApiGateway.class, notificationURL);
    }
}