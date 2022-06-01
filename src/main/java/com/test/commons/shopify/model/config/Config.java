package com.test.commons.shopify.model.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Config {

    private String lightboxURL;
    private String lightboxNotificationURL;
    private String jsonContentType;
    private String awsLambdaReadEvent;
    private String defaultCharSet;

}
