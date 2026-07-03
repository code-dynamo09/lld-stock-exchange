package com.algo.stock_exchange.models;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Trade {

    @Builder.Default
    private String tradeId = UUID.randomUUID().toString();

    @NotBlank(message = "buyerOrderId is required")
    private String buyerOrderId;

    @NotBlank(message = "sellerOrderId is required")
    private String sellerOrderId;

    @NotBlank(message = "stockSymbol is required")
    private String stockSymbol;

    @NotBlank(message = "Quantity is required")
    private Integer quantity;

    @NotBlank(message = "price is required")
    private Double price;

    @Builder.Default
    private Date createdTimestamp = Date.from(Instant.now());


}
