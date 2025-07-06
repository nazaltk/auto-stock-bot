package com.nazal.StockAutomationBot.repository;

import com.nazal.StockAutomationBot.entity.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {}

