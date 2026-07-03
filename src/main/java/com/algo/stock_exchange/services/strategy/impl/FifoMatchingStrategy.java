package com.algo.stock_exchange.services.strategy.impl;

import com.algo.stock_exchange.models.Order;
import com.algo.stock_exchange.models.OrderStatus;
import com.algo.stock_exchange.models.OrderType;
import com.algo.stock_exchange.models.Trade;
import com.algo.stock_exchange.services.strategy.OrderMatchingStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class FifoMatchingStrategy implements OrderMatchingStrategy {
    @Override
    public List<Trade> matchOrder(Order newOrder, List<Order> existingOrders) {
        if(newOrder.getOrderType() == OrderType.BUY) {
            return processBuyOrder(newOrder, existingOrders);
        } else{
            return processSellOrder(newOrder, existingOrders);
        }

    }

    @Override
    public String getStrategy() {
        return "FIFO";
    }

//    Exchanges use two primary criteria to determine which orders execute first:
//    Price Priority: The highest buy price and lowest sell price always execute first.
//    Time Priority: If prices are equal, the order that arrived earliest executes first.

    private List<Trade> processBuyOrder(Order buyOrder, List<Order> existingOrders) {
        List<Trade> trades = new ArrayList<>();
        List<Order> filteredOrders = existingOrders.stream()
                                        .filter(order -> order.getStockSymbol().equals(buyOrder.getStockSymbol()))
                                        .filter(order -> order.getOrderType().equals(OrderType.SELL))
                                        .filter(order -> order.getPrice() <= buyOrder.getPrice())
                                        .filter(order -> order.getOrderStatus().equals(OrderStatus.ACCEPTED))
                                        .sorted(Comparator.comparing(Order::getPrice) // sorting order by price
                                                .thenComparing(Order::getCreatedTimestamp)) // and then by time
                                        .collect(Collectors.toList());



        int remainingQty = buyOrder.getRemainingQuantity();

        for(Order sellOrder : filteredOrders) {
            if(remainingQty <= 0) break;

            int tradeQty = Math.min(remainingQty, sellOrder.getRemainingQuantity());

            Double tradePrice = sellOrder.getPrice();


            Trade trade = Trade.builder()
                    .buyerOrderId(buyOrder.getOrderId())
                    .sellerOrderId(sellOrder.getOrderId())
                    .stockSymbol(buyOrder.getStockSymbol())
                    .price(tradePrice)
                    .quantity(tradeQty)
                    .createdTimestamp(Date.from(Instant.now()))
                    .build();

            trades.add(trade);

            remainingQty = remainingQty - tradeQty;
            buyOrder.setFilledQuantity(buyOrder.getFilledQuantity() + tradeQty);
            buyOrder.setRemainingQuantity(buyOrder.getRemainingQuantity()-tradeQty);

            sellOrder.setFilledQuantity(sellOrder.getFilledQuantity() + tradeQty);
            sellOrder.setRemainingQuantity(sellOrder.getRemainingQuantity() - tradeQty);

        }

        return trades;

    }


    private List<Trade> processSellOrder(Order sellOrder, List<Order> existingOrders) {
        List<Trade> trades = new ArrayList<>();

        List<Order> filteredOrders = existingOrders.stream()
                .filter(order -> order.getStockSymbol().equals(sellOrder.getStockSymbol()))
                .filter(order -> order.getOrderType().equals(OrderType.BUY))
                .filter(order -> order.getPrice() >= sellOrder.getPrice())
                .filter(order -> order.getOrderStatus().equals(OrderStatus.ACCEPTED))
                .sorted(Comparator.comparing(Order::getPrice, Comparator.reverseOrder())
                        .thenComparing(Order::getCreatedTimestamp)) // and then by time
                .collect(Collectors.toList());

        int remainingQty = sellOrder.getRemainingQuantity();

        for(Order buyOrder : filteredOrders) {
            if(remainingQty <= 0) break;

            int tradeQty = Math.min(remainingQty, buyOrder.getRemainingQuantity());

            Double tradePrice = buyOrder.getPrice();


            Trade trade = Trade.builder()
                    .buyerOrderId(buyOrder.getOrderId())
                    .sellerOrderId(sellOrder.getOrderId())
                    .stockSymbol(sellOrder.getStockSymbol())
                    .price(tradePrice)
                    .quantity(tradeQty)
                    .createdTimestamp(Date.from(Instant.now()))
                    .build();

            trades.add(trade);

            remainingQty = remainingQty - tradeQty;
            buyOrder.setFilledQuantity(buyOrder.getFilledQuantity() + tradeQty);
            buyOrder.setRemainingQuantity(buyOrder.getRemainingQuantity()-tradeQty);

            sellOrder.setFilledQuantity(sellOrder.getFilledQuantity() + tradeQty);
            sellOrder.setRemainingQuantity(sellOrder.getRemainingQuantity() - tradeQty);

        }

        return trades;

    }
}
