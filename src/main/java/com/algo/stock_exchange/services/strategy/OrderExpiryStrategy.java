package com.algo.stock_exchange.services.strategy;

import com.algo.stock_exchange.models.Order;

public interface OrderExpiryStrategy {

    Boolean checkOrderExpiry(Order order);
}
