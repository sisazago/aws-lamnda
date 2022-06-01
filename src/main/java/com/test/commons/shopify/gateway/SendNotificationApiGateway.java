package com.test.commons.shopify.gateway;

import com.test.commons.shopify.model.request.UpdateOrderRequest;
import feign.Headers;
import feign.RequestLine;

@Headers("Content-Type: application/json")
public interface SendNotificationApiGateway {

    @RequestLine("POST /send-notification")
    void sendNotification(UpdateOrderRequest request);

    @RequestLine("PUT /update-shipping")
    void updateShippingStatusOrder(UpdateOrderRequest request);
}
