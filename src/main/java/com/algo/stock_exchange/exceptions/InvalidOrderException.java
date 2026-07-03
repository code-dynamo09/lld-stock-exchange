package com.algo.stock_exchange.exceptions;

public class InvalidOrderException extends TradingExceptionHandler{

    public InvalidOrderException(String orderID) {
        super("Order does not exist with orderID: "+orderID);
    }
}
