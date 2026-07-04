package com.algo.stock_exchange.services;

import com.algo.stock_exchange.models.Trade;
import com.algo.stock_exchange.repositories.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TradeServiceImpl implements TradeService{

    private TradeRepository tradeRepository;

    @Autowired
    public TradeServiceImpl(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }


    @Override
    public void saveTrade(Trade trade) {
        this.tradeRepository.saveTrade(trade);
    }

    @Override
    public Optional<Trade> getTradeById(String tradeId) {
        return this.tradeRepository.getTradeById(tradeId);
    }

    @Override
    public List<Trade> getTradeByStockSymbol(String stockSymbol) {
        return this.tradeRepository.getTradeByStockSymbol(stockSymbol);
    }
}
