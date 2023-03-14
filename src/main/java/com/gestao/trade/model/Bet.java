package com.gestao.trade.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Table(name = "bets")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Bet {

    public Bet(Long id, String dayOfWeek, Long date, String homeTeam, String awayTeam, String backOrLay, String market, String selection, Float odd, BigDecimal liability, BigDecimal grossProfit, BigDecimal netProfit, Float roi) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.backOrLay = backOrLay;
        this.market = market;
        this.selection = selection;
        this.odd = odd;
        this.liability = liability;
        this.netProfit = netProfit;
        this.grossProfit = grossProfit;
        this.roi = roi;
    }

    @Id
    private Long id;
    private String dayOfWeek;
    private Long date;
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
    private BigDecimal netProfit;
    private Float roi;
}