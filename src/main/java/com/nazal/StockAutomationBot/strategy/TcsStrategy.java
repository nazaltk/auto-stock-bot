package com.nazal.StockAutomationBot.strategy;


import com.nazal.StockAutomationBot.service.StrategyService;
import com.nazal.StockAutomationBot.service.TwelveDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.ta4j.core.*;
import org.ta4j.core.num.DecimalNum;


@Component
@RequiredArgsConstructor
public class TcsStrategy implements TradingStrategy {

    private final TwelveDataService dataService;
    private final StrategyService strategyService;

    @Override
    public void evaluate() {
        try {
            BarSeries series = dataService.fetchTimeSeries(getSymbol());
            Strategy strategy = strategyService.buildStrategy(series);
            TradingRecord record = new BaseTradingRecord();

            for (int i = 0; i < series.getBarCount(); i++) {
                Bar bar = series.getBar(i);
                if (strategy.shouldEnter(i)) {
                    System.out.printf("[%s] BUY TCS @ %.2f\n", bar.getEndTime(), bar.getClosePrice().doubleValue());
                    record.enter(i, bar.getClosePrice(), DecimalNum.valueOf(1));
                } else if (strategy.shouldExit(i)) {
                    System.out.printf("[%s] SELL TCS @ %.2f\n", bar.getEndTime(), bar.getClosePrice().doubleValue());
                    record.exit(i, bar.getClosePrice(), DecimalNum.valueOf(1));
                }
            }
        } catch (Exception e) {
            System.err.println("TCS Strategy error: " + e.getMessage());
        }
    }

    @Override
    public String getSymbol() {
        return "TCS";
    }
}