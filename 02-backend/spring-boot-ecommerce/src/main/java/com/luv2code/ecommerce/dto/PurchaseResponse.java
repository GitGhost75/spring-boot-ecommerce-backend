package com.luv2code.ecommerce.dto;

import lombok.Data;

@Data
public class PurchaseResponse {

    // lombok generates constructor with fields for final fields
    private final String orderTrackingNumber;
}
