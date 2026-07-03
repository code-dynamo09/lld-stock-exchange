package com.algo.stock_exchange.exceptions;

public class OrderNotFoundException extends TradingExceptionHandler{

    public OrderNotFoundException(String orderId) {
        super("Order not found with order id: "+ orderId);
    }
}
