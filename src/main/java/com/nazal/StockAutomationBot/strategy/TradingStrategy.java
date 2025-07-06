package com.nazal.StockAutomationBot.strategy;

public interface TradingStrategy {
    void evaluate();
    String getSymbol();
}
