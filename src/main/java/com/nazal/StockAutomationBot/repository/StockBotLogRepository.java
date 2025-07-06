package com.nazal.StockAutomationBot.repository;

import com.nazal.StockAutomationBot.entity.StockBotLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockBotLogRepository extends JpaRepository<StockBotLog, Long> {
}