package com.test.commons.shopify.model.shopify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fulfillment {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("service")
    private String service;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("tracking_company")
    private String trackingCompany;
    @JsonProperty("shipment_status")
    private String shipmentStatus;
    @JsonProperty("location_id")
    private String locationId;
    @JsonProperty("origin_address")
    private String originAddress;
    @JsonProperty("email")
    private String email;
    @JsonProperty("tracking_number")
    private String trackingNumber;
    @JsonProperty("tracking_numbers")
    private List<String> trackingNumbers = null;
    @JsonProperty("tracking_url")
    private String trackingUrl;
    @JsonProperty("tracking_urls")
    private List<String> trackingUrls = null;
    @JsonProperty("name")
    private String name;
    @JsonProperty("admin_graphql_api_id")
    private String adminGraphqlApiId;
}
