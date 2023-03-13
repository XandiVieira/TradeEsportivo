package com.gestao.trade.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Table(name = "bets")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Bet {

    public Bet(Long id, LocalDate date, String dayOfWeek, String homeTeam, String awayTeam, String backOrLay, String market, String selection, Float odd, BigDecimal liability, BigDecimal grossProfit) {
        this.id = id;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.backOrLay = backOrLay;
        this.market = market;
        this.selection = selection;
        this.odd = odd;
        this.liability = liability;
        this.grossProfit = grossProfit;
    }

    @Id
    private Long id;
    private LocalDate date;
    private String dayOfWeek;
    private String championship;
    private String homeTeam;
    private String awayTeam;
    private String backOrLay;
    private String method;
    private String market;
    private String selection;
    private Float odd;
    private BigDecimal liability;
    private BigDecimal grossProfit;
}