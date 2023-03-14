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

    public Bet(Long id, Long date, String homeTeam, String awayTeam, String backOrLay, String market, String selection, Float odd, BigDecimal liability, BigDecimal grossProfit) {
        this.id = id;
        this.date = date;
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
}