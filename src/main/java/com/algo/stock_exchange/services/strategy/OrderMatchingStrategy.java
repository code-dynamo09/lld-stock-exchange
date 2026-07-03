package com.algo.stock_exchange.services.strategy;

import com.algo.stock_exchange.models.Order;
import com.algo.stock_exchange.models.Trade;

import java.util.List;

public interface OrderMatchingStrategy {

    List<Trade> matchOrder(Order newOrder, List<Order> existingOrders);

    String getStrategy();
}
