package com.test.commons.shopify.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateOrderRequest {

    private String orderId;

    private String shipmentStatus;

    private String message;
}
