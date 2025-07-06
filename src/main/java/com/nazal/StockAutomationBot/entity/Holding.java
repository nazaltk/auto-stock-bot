package com.nazal.StockAutomationBot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "holdings")
@Getter
@Setter
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private double buyPrice;
    private int quantity;

    @Column(name = "buy_time")
    private LocalDateTime buyTime;

}