package com.algo.stock_exchange.services.helpers;

import com.algo.stock_exchange.models.Order;

import java.util.List;
import java.util.Optional;

public interface IorderBook {

    void addOrder(Order order);
    Boolean removeOrder(Order order);
    Boolean updateOrder(Order updatedOrder);

    List<Order> getOrders(String stockSymbol);

    Optional<Order> getOrderBySymbol(String stockSymbol);


}
