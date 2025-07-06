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
@RequiredArgsConstructor
@Slf4j
public class TcsStrategy implements TradingStrategy {

    private final TwelveDataService dataService;
    private final StrategyService strategyService;
    private final HoldingRepository holdingRepository;
    private final AccountBalanceRepository accountBalanceRepository;

    @Autowired
    private MysqlLoggerService mysqlLoggerService;

    public void evaluate() {
        try {
            BarSeries series = dataService.fetchTimeSeries(getSymbol());
            Strategy strategy = strategyService.buildStrategy(series);
            TradingRecord record = new BaseTradingRecord();

            for (int i = 0; i < series.getBarCount(); i++) {
                Bar bar = series.getBar(i);
                if (strategy.shouldEnter(i)) {
                    double price = bar.getClosePrice().doubleValue();
                    int qty = 1;  // or based on your balance

                    // Check if already holding
                    boolean alreadyHolding = !holdingRepository.findBySymbol(getSymbol()).isEmpty();
                    if (alreadyHolding) {
                        System.out.printf("[%s] Already holding %s, skipping BUY.\n", bar.getEndTime(), getSymbol());
                        return;
                    }

                    // Check if balance is enough
                    AccountBalance balance = accountBalanceRepository.findById(1L).orElse(null);
                    if (balance == null || balance.getAvailableBalance() < price * qty) {
                        System.out.printf("[%s] Not enough balance to buy %s\n", bar.getEndTime(), getSymbol());
                        return;
                    }

                    // Place order (simulated)
                    System.out.printf("[%s] BUY %s @ %.2f\n", bar.getEndTime(), getSymbol(), price);
                    record.enter(i, bar.getClosePrice(), DecimalNum.valueOf(qty));

                    // Save holding
                    Holding holding = new Holding();
                    holding.setSymbol(getSymbol());
                    holding.setBuyPrice(price);
                    holding.setQuantity(qty);
                    holding.setBuyTime(bar.getEndTime().toLocalDateTime());
                    holdingRepository.save(holding);

                    // Deduct balance
                    balance.setAvailableBalance(balance.getAvailableBalance() - price * qty);
                    accountBalanceRepository.save(balance);
                } else if (strategy.shouldExit(i)) {
                    String symbol = getSymbol();
                    List<Holding> holdings = holdingRepository.findBySymbol(symbol);

                    if (!holdings.isEmpty()) {
                        Holding holding = holdings.get(0);
                        double buyPrice = holding.getBuyPrice();
                        int quantity = holding.getQuantity();
                        double sellPrice = bar.getClosePrice().doubleValue();

                        double grossProfit = (sellPrice - buyPrice) * quantity;
                        double brokerageCost = 40.0;
                        double netProfit = grossProfit - brokerageCost;

                        if (netProfit > 0) {
                            System.out.printf("[%s] SELL %s @ %.2f | Buy: %.2f | Qty: %d | Net Profit: ₹%.2f\n",
                                    bar.getEndTime(), symbol, sellPrice, buyPrice, quantity, netProfit);

                            record.exit(i, bar.getClosePrice(), DecimalNum.valueOf(quantity));

                            // Add balance back
                            AccountBalance balance = accountBalanceRepository.findById(1L).orElse(null);
                            if (balance != null) {
                                double totalSellAmount = sellPrice * quantity;
                                balance.setAvailableBalance(balance.getAvailableBalance() + totalSellAmount);
                                accountBalanceRepository.save(balance);
                            }

                            // Remove holding
                            holdingRepository.deleteAll(holdings);
                        } else {
                            System.out.printf("[%s] EXIT IGNORED: No Net Profit (%.2f - charges ₹%.2f)\n",
                                    bar.getEndTime(), grossProfit, brokerageCost);
                        }
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