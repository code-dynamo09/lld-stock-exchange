package com.algo.stock_exchange.services;

import com.algo.stock_exchange.models.Order;
import com.algo.stock_exchange.models.OrderStatus;
import com.algo.stock_exchange.models.OrderType;
import com.algo.stock_exchange.models.Trade;
import com.algo.stock_exchange.services.helpers.IorderBook;
import com.algo.stock_exchange.services.strategy.OrderMatchingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TradingServiceImpl implements TradingService {

    private IorderBook orderBook;
    private OrderMatchingStrategy orderMatchingStrategy;

    private final ExecutorService executorService;

    public TradingServiceImpl(IorderBook orderBook, OrderMatchingStrategy orderMatchingStrategy) {
        this.orderBook = orderBook;
        this.orderMatchingStrategy = orderMatchingStrategy;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public Order placeOrder(Order order) {

        order.setCreatedTimestamp(new Date(System.currentTimeMillis()));
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setFilledQuantity(0);

        this.orderBook.addOrder(order);
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                executeOrders(order);
            } catch (Exception e) {
                log.error("Exception occured : {}", e.getLocalizedMessage());
            }
        }, this.executorService);

        return order;
    }

    private void executeOrders(Order order) {
        String symbol = order.getStockSymbol();
        List<Order> orderList = this.orderBook.getOrders(symbol);
        List<Order> filteredOrders = orderList.stream()
                .filter(o-> !order.getOrderId().equals(o.getOrderId()))
                .collect(Collectors.toList());

        List<Trade> executedTrades = this.orderMatchingStrategy.matchOrder(order, filteredOrders);
        if(executedTrades.isEmpty()) return;

        for(Trade trade: executedTrades) {
            // save trades in DB
        }
        this.orderBook.updateOrder(order);


        for(Trade trade: executedTrades) {
            String otherOrderId = order.getOrderType() == OrderType.BUY ? trade.getSellerOrderId()
                    : trade.getBuyerOrderId();
            Optional<Order> otherTradedOrder = this.orderBook.getOrderById(otherOrderId);
//            otherTradedOrder.ifPresent(value -> this.orderBook.updateOrder(value));
            otherTradedOrder.ifPresent(orderBook::updateOrder);


        }

        log.info("Order matched successfully");

    }
}
