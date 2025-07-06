package com.nazal.StockAutomationBot.controller;


import com.nazal.StockAutomationBot.entity.Holding;
import com.nazal.StockAutomationBot.entity.StockBotLog;
import com.nazal.StockAutomationBot.service.HoldingsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class HoldingsController {

    private final HoldingsService holdingsService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Holding> holdings = holdingsService.getAllHoldings();
        List<StockBotLog> logs = holdingsService.getAllLogs();
        model.addAttribute("logs", logs);
        model.addAttribute("holdings", holdings);

        return "dashboard"; // maps to dashboard.html
    }

}
