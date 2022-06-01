package com.test.commons.shopify.ex;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class ShopifyWebhookException extends RuntimeException {

    public ShopifyWebhookException(String message, Throwable cause) {
        super(message, cause);
    }
}
