package com.algo.stock_exchange.services;

import com.algo.stock_exchange.models.Trade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TradeService {

    void saveTrade(Trade trade);

    Optional<Trade> getTradeById(String tradeId);

    List<Trade> getTradeByStockSymbol(String stockSymbol);
}
