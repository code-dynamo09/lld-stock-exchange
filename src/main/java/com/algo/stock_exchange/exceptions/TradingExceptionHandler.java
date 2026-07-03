package com.algo.stock_exchange.exceptions;

public class TradingExceptionHandler extends Exception {

    private String message;


    public TradingExceptionHandler(String message) {
        super(message);
        this.message = message;
    }

    public TradingExceptionHandler(String message, Throwable throwable) {
        super(message, throwable);
        this.message = message;
    }

}
