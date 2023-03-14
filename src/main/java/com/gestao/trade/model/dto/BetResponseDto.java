package com.gestao.trade.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class BetResponseDto {

    @JsonProperty("Dia da semana")
    private String dayOfWeek;
    @JsonProperty("Data")
    private String date;
    @JsonProperty("Campeonato")
    private String championship;
    @JsonProperty("Mandante")
    private String homeTeam;
    @JsonProperty("Visitante")
    private String awayTeam;
    @JsonProperty("Back/Lay")
    private String backOrLay;
    @JsonProperty("Mercado")
    private String market;
    @JsonProperty("Seleção")
    private String selection;
    @JsonProperty("Método")
    private String method;
    @JsonProperty("Odd")
    private Float odd;
    @JsonProperty("Entrada")
    private BigDecimal liability;
    @JsonProperty("Lucro Bruto")
    private BigDecimal grossProfit;
    @JsonProperty("Lucro líquido")
    private BigDecimal netProfit;
    @JsonProperty("ROI")
    private Float returnOverInvestment;
    @JsonProperty("Retorno sob a banca")
    private Float returnOverTotal;
}