package com.nazal.StockAutomationBot.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stock_bot_log")
@Data
public class StockBotLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;
    private String log_time;
    private String symbol;
    private String action;
    private int qty;
    private double price;
}
