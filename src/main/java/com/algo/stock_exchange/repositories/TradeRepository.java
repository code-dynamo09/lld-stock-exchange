package com.algo.stock_exchange.repositories;

import com.algo.stock_exchange.models.Trade;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Repository
public class TradeRepository {
    private ConcurrentMap<String, Trade> tradeMap;

    public TradeRepository() {
        this.tradeMap = new ConcurrentHashMap<>();
    }


    public void saveTrade(Trade trade) {
        this.tradeMap.put(trade.getTradeId(), trade);
    }

    public Optional<Trade> getTradeById(String tradeId) {
        return Optional.ofNullable(tradeMap.get(tradeId));

    }

    public List<Trade> getTradeByStockSymbol(String stockSymbol) {
        return this.tradeMap.values().stream()
                .filter(trade -> trade.getStockSymbol().equals(stockSymbol))
                .collect(Collectors.toList());
    }
}
