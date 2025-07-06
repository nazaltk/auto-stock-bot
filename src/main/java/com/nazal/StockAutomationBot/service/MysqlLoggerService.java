package com.nazal.StockAutomationBot.service;

import com.nazal.StockAutomationBot.entity.StockBotLog;
import com.nazal.StockAutomationBot.repository.StockBotLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class MysqlLoggerService {

    @Autowired
    private StockBotLogRepository repository;

    public void log(String symbol, String action, int qty, double price) {
        StockBotLog log = new StockBotLog();
        log.setDate(LocalDate.now().toString());
        log.setLog_time(LocalTime.now().toString());
        log.setSymbol(symbol);
        log.setAction(action);
        log.setQty(qty);
        log.setPrice(price);

        repository.save(log);

        System.out.println("✅ Signal logged to MySQL: " + log);
    }
}
