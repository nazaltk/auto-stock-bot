package com.nazal.StockAutomationBot.strategy;


import com.nazal.StockAutomationBot.entity.AccountBalance;
import com.nazal.StockAutomationBot.entity.Holding;
import com.nazal.StockAutomationBot.repository.AccountBalanceRepository;
import com.nazal.StockAutomationBot.repository.HoldingRepository;
import com.nazal.StockAutomationBot.service.MysqlLoggerService;
import com.nazal.StockAutomationBot.service.StrategyService;
import com.nazal.StockAutomationBot.service.TwelveDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ta4j.core.*;
import org.ta4j.core.num.DecimalNum;

import java.util.List;


@Component
@Slf4j
public class TcsStrategy extends TradingStrategy {

    @Autowired
    private MysqlLoggerService mysqlLoggerService;

    public TcsStrategy(TwelveDataService dataService, StrategyService strategyService, HoldingRepository holdingRepository, AccountBalanceRepository accountBalanceRepository) {
        super(dataService, strategyService, holdingRepository, accountBalanceRepository);
    }

    @Override
    public String getSymbol() {
        return "TCS";
    }
}