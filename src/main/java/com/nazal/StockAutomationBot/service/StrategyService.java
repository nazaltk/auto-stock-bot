package com.nazal.StockAutomationBot.service;

import org.springframework.stereotype.Service;
import org.ta4j.core.*;
import org.ta4j.core.indicators.*;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.rules.*;

@Service
public class StrategyService {
    public Strategy buildStrategy(BarSeries series) {
        ClosePriceIndicator close = new ClosePriceIndicator(series);
        RSIIndicator rsi = new RSIIndicator(close, 14);
        MACDIndicator macd = new MACDIndicator(close, 12, 26);
        EMAIndicator signal = new EMAIndicator(macd, 9);

        Rule entry = new UnderIndicatorRule(rsi, 30).and(new CrossedUpIndicatorRule(macd, signal));
        Rule exit = new OverIndicatorRule(rsi, 70).or(new CrossedDownIndicatorRule(macd, signal));

        return new BaseStrategy(entry, exit);
    }
}
