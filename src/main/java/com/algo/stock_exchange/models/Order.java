package com.algo.stock_exchange.models;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Order {

    @Builder.Default
    private String orderId = UUID.randomUUID().toString();

    @NotBlank(message = "UserId is required")
    private String userId;

    @NotBlank(message = "OrderType is required")
    private OrderType orderType;

    @NotBlank(message = "stockSymbol is required")
    private String stockSymbol;

    @NotBlank(message = "Quantity is required")
    private Integer quantity;

    @NotBlank(message = "price is required")
    private Double price;

    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.ACCEPTED;

    @Builder.Default
    private Integer filledQuantity = 0;

    @Builder.Default
    private Integer remainingQuantity = 0;

    private Date createdTimestamp;

}
