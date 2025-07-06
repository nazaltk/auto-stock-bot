package com.nazal.StockAutomationBot.strategy;


import com.nazal.StockAutomationBot.service.MysqlLoggerService;
import com.nazal.StockAutomationBot.service.StrategyService;
import com.nazal.StockAutomationBot.service.TwelveDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ta4j.core.*;
import org.ta4j.core.num.DecimalNum;


@Component
@RequiredArgsConstructor
@Slf4j
public class TcsStrategy implements TradingStrategy {

    private final TwelveDataService dataService;
    private final StrategyService strategyService;

    @Autowired
    private MysqlLoggerService mysqlLoggerService;

    public void evaluate() {
        try {
            BarSeries series = dataService.fetchTimeSeries(getSymbol());
            Strategy strategy = strategyService.buildStrategy(series);
            TradingRecord record = new BaseTradingRecord();

            double bufferPercent = 0.003; // 0.3% commission + slippage buffer

            for (int i = 0; i < series.getBarCount(); i++) {
                Bar bar = series.getBar(i);
                double currentPrice = bar.getClosePrice().doubleValue();

                if (strategy.shouldEnter(i)) {
                    // Only BUY if potential upside > 0.3%
                    double previousPrice = i > 0 ? series.getBar(i - 1).getClosePrice().doubleValue() : currentPrice;
                    double expectedProfit = currentPrice - previousPrice;

                    if (expectedProfit > currentPrice * bufferPercent) {
                        System.out.printf("[%s] ✅ BUY TCS @ %.2f (expected profit: ₹%.2f)\n",
                                bar.getEndTime(), currentPrice, expectedProfit);
                        mysqlLoggerService.log("TCS.NS", "BUY", 1, currentPrice);
                        record.enter(i, bar.getClosePrice(), DecimalNum.valueOf(1));
                    } else {
                        System.out.printf("[%s] ❌ Skipped BUY TCS @ %.2f (not enough to cover costs)\n",
                                bar.getEndTime(), currentPrice);
                    }

                } else if (strategy.shouldExit(i)) {
                    double entryPrice = record.getCurrentPosition().getEntry().getNetPrice().doubleValue();
                    double expectedProfit = currentPrice - entryPrice;

                    if (expectedProfit > currentPrice * bufferPercent) {
                        System.out.printf("[%s] ✅ SELL TCS @ %.2f (profit: ₹%.2f)\n",
                                bar.getEndTime(), currentPrice, expectedProfit);
                        mysqlLoggerService.log("TCS.NS", "SELL", 1, currentPrice);
                        record.exit(i, bar.getClosePrice(), DecimalNum.valueOf(1));
                    } else {
                        System.out.printf("[%s] ❌ Skipped SELL TCS @ %.2f (not enough to cover costs)\n",
                                bar.getEndTime(), currentPrice);
                    }
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