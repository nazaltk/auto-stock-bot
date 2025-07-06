package com.nazal.StockAutomationBot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account_balance")
@Getter
@Setter
public class AccountBalance {
    @Id
    private Long id = 1L;  // single-row table

    private double availableBalance;
}