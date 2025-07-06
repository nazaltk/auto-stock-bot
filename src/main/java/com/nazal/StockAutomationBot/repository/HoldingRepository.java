package com.nazal.StockAutomationBot.repository;

import com.nazal.StockAutomationBot.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoldingRepository extends JpaRepository<Holding, Long> {
    List<Holding> findBySymbol(String symbol);
}
