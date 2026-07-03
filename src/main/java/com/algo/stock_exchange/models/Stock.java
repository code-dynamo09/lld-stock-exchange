package com.algo.stock_exchange.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Builder.Default
    private String stockId = UUID.randomUUID().toString();

    @Builder.Default
    private String stockSymbol;

    private String stockName;

    private Double stockPrice;

}
