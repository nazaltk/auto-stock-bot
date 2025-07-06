package com.nazal.StockAutomationBot.service;

import com.nazal.StockAutomationBot.entity.Holding;
import com.nazal.StockAutomationBot.entity.StockBotLog;
import com.nazal.StockAutomationBot.repository.HoldingRepository;
import com.nazal.StockAutomationBot.repository.StockBotLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HoldingsService {

    private final HoldingRepository holdingRepository;
    private final StockBotLogRepository stockBotLogRepository;

    public List<Holding> getAllHoldings(){
        return holdingRepository.findAll();
    }

    public List<StockBotLog> getAllLogs() {
        return stockBotLogRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }
}
