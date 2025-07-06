package com.nazal.StockAutomationBot.scheduler;

import com.nazal.StockAutomationBot.strategy.TradingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AutomationScheduler {

    private final List<TradingStrategy> strategies;

    @Scheduled(cron = "0 */5 9-15 ? * MON-FRI", zone = "Asia/Kolkata")
    //@Scheduled(cron = "* */5 * * * * ")
    public void runAllStrategies() {
        for (TradingStrategy strategy : strategies) {
            System.out.println("Running strategy for: " + strategy.getSymbol());
            strategy.evaluate();
        }
    }
}