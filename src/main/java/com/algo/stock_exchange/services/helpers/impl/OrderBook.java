package com.algo.stock_exchange.services.helpers.impl;

import com.algo.stock_exchange.models.Order;
import com.algo.stock_exchange.services.helpers.IorderBook;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;


@Slf4j
public class OrderBook implements IorderBook {

    private ConcurrentMap<String, List<Order>> orderBookMap;
    private ConcurrentMap<String, ReadWriteLock> stockSymbolLocks;

    public OrderBook() {
        this.orderBookMap = new ConcurrentHashMap<>();
        this.stockSymbolLocks = new ConcurrentHashMap<>();
    }
    @Override
    public void addOrder(Order order) {
        String stockSymbol = order.getStockSymbol();

        ReadWriteLock lock = this.stockSymbolLocks.getOrDefault(stockSymbol, new ReentrantReadWriteLock());

        // we will acquire write lock since we are adding Order
        lock.writeLock().lock();

        try {
            this.orderBookMap.computeIfAbsent(stockSymbol, k -> new ArrayList<>()).add(order);
            log.info("Order added to order book : {}", order.toString());
        } finally {
            lock.writeLock().unlock();
        }

    }

    @Override
    public Boolean removeOrder(Order order) {
        String stockSymbol = order.getStockSymbol();

        ReadWriteLock lock = this.stockSymbolLocks.computeIfAbsent(stockSymbol, k -> new ReentrantReadWriteLock());

        // we will acquire write lock since we are adding Order
        lock.writeLock().lock();

        try {
            List<Order> orders = this.orderBookMap.get(stockSymbol);
            if(orders != null) {
                boolean removed = orders.remove(order);
                if(removed) {
                    log.info("Order removed from order book : {}", order.toString());
                } else {
                    log.info("Order not found in order book : {}", order.toString());
                }
                return removed;
            }
        } finally {
            lock.writeLock().unlock();
        }
        return false;
    }

    @Override
    public Boolean updateOrder(Order updatedOrder) {
        String stockSymbol = updatedOrder.getStockSymbol();

        ReadWriteLock lock = this.stockSymbolLocks.computeIfAbsent(stockSymbol, k -> new ReentrantReadWriteLock());

        // we will acquire write lock since we are adding Order
        lock.writeLock().lock();

        try {
            List<Order> orders = this.orderBookMap.get(stockSymbol);
            if(orders != null) {
                List<Order> updatedOrders
                        = orders.stream()
                        .map(order
                                -> updatedOrder.getOrderId().equals(order.getOrderId())
                                    ? updatedOrder : order)
                        .toList();
                this.orderBookMap.put(updatedOrder.getStockSymbol(), updatedOrders);
                return true;
            }

        } finally {
            lock.writeLock().unlock();
        }
        return false;
    }

    @Override
    public List<Order> getOrders(String stockSymbol) {
        ReadWriteLock lock = this.stockSymbolLocks.computeIfAbsent(stockSymbol, k -> new ReentrantReadWriteLock());
        try {
            lock.readLock().lock();
            return this.orderBookMap.getOrDefault(stockSymbol, new ArrayList<>());
        } catch (RuntimeException e) {
            log.error("Exception occured while locking: {}", e.getLocalizedMessage());
        } finally {
            lock.readLock().unlock();
        }
        return Collections.emptyList();

    }

    @Override
    public Optional<Order> getOrderBySymbol(String stockSymbol) {

        ReadWriteLock lock = this.stockSymbolLocks.computeIfAbsent(stockSymbol, k -> new ReentrantReadWriteLock());
        lock.readLock().lock();
        try {
            return this.orderBookMap.get(stockSymbol).stream().findFirst();
        } catch (RuntimeException e) {
            log.error("Exception occured while locking: {}", e.getLocalizedMessage());
        } finally {
            lock.readLock().unlock();
        }
        return Optional.empty();
    }

}
