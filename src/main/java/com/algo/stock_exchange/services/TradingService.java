package com.algo.stock_exchange.services;

import com.algo.stock_exchange.models.Order;
import org.springframework.stereotype.Service;

@Service
public interface TradingService {

    Order placeOrder(Order order);
}
