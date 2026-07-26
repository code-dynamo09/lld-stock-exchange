package com.algo.stock_exchange.controllers;

import com.algo.stock_exchange.dto.OrderRequest;
import com.algo.stock_exchange.models.Order;
import com.algo.stock_exchange.services.TradingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/trading")
public class TradingController {

    private final TradingService tradingService;


    @PostMapping(/)
    public ResponseEntity<Order> placeOrder(OrderRequest orderRequest) {
        Order order = buildOrder(orderRequest);
        tradingService.placeOrder(order);

        return ResponseEntity.ok()
    }

    private Order buildOrder(OrderRequest orderRequest) {
        return Order.builder()
                .orderType(orderRequest.getOrderType())
                .stockSymbol(orderRequest.getStockSymbol())
                .createdTimestamp(new Date(System.currentTimeMillis()))
                .price(orderRequest.getPrice())
                .quantity(orderRequest.getQuantity())
                .userId(orderRequest.getUserId())
                .build();


    }

}
