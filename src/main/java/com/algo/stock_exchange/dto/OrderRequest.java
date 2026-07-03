package com.algo.stock_exchange.dto;

import com.algo.stock_exchange.models.OrderType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotBlank(message = "userId is mandatory required")
    private String userId;

    @NotBlank(message = "StockSymbol is mandatory required")
    private String stockSymbol;

    @NotBlank(message = "OrderType is mandatory required")
    private OrderType orderType;

    @NotBlank(message = "Quantity is mandatory required")
    private int quantity;

    @NotBlank(message = "Price is mandatory required")
    private Double price;

}


