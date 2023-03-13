package com.gestao.trade.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class BetDtoResponse {

    private String date;
    private String dayOfWeek;
    private String championship;
    private String homeTeam;
    private String awayTeam;
    private String backOrLay;
    private String market;
    private String betDescription;
    private String method;
    private Float odd;
    private BigDecimal investment;
    private BigDecimal grossProfit;
    private BigDecimal netProfit;
    private Float returnOverInvestment;
    private Float returnOverTotal;
}
