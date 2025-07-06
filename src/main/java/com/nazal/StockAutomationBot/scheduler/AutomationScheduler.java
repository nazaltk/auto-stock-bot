package com.nazal.StockAutomationBot.scheduler;

import com.nazal.StockAutomationBot.service.MysqlLoggerService;
import com.nazal.StockAutomationBot.strategy.TradingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutomationScheduler {

    private final List<TradingStrategy> strategies;

    @Autowired
    MysqlLoggerService mysqlLoggerService;

    @Scheduled(cron = "0 */5 9-15 ? * MON-FRI", zone = "Asia/Kolkata")
    //@Scheduled(cron = "* */5 * * * * ")
    public void runAllStrategies() {
        for (TradingStrategy strategy : strategies) {
            System.out.println("Running strategy for: " + strategy.getSymbol());
            strategy.evaluate();
        }
    }

    @Scheduled(fixedRate = 60000)
    public void pingToKeepUp() {
        log.info("yes, App is Up");
    }
}