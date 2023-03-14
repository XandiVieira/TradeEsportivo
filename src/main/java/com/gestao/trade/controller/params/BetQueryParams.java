package com.gestao.trade.controller.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BetQueryParams {

    private String championship;
    private String dayOfWeek;
    private String backOrLay;
    private String method;
    private String market;
    private Float roiMin;
    private Float roiMax;
    private Float oddMin;
    private Float oddMax;
    private Float netProfitMin;
    private Float netProfitMax;
    private Float liabilityMin;
    private Float liabilityMax;
    private String team;
    private int pageSize = 10;
    private int pageNumber = 0;
    private String sortBy = "date";
    private String sortDirection = "desc";
}