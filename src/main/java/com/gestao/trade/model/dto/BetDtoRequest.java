package com.gestao.trade.model.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BetDtoRequest {

    private String market;
    private String selection;
    private String bidType;
    private String betId;
    private String betPlaced;
    private String persistence;
    private String requiredOdds;
    private String stopLoss;
    private String liability;
    private String matchedOdds;
    private String profitLoss;

    public BetDtoRequest(String market, String selection, String bidType, String betId, String betPlaced,
                         String persistence, String requiredOdds, String stopLoss, String liability, String matchedOdds, String profitLoss) {
        this.market = market;
        this.selection = selection;
        this.bidType = bidType;
        this.betId = betId;
        this.betPlaced = betPlaced;
        this.persistence = persistence;
        this.requiredOdds = requiredOdds;
        this.stopLoss = stopLoss;
        this.liability = liability;
        this.matchedOdds = matchedOdds;
        this.profitLoss = profitLoss;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public String getBetId() {
        return betId;
    }

    public void setBetId(String betId) {
        this.betId = betId;
    }

    public String getBetPlaced() {
        return betPlaced;
    }

    public void setBetPlaced(String betPlaced) {
        this.betPlaced = betPlaced;
    }

    public String getPersistence() {
        return persistence;
    }

    public void setPersistence(String persistence) {
        this.persistence = persistence;
    }

    public String getRequiredOdds() {
        return requiredOdds;
    }

    public void setRequiredOdds(String requiredOdds) {
        this.requiredOdds = requiredOdds;
    }

    public String getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(String stopLoss) {
        this.stopLoss = stopLoss;
    }

    public String getLiability() {
        return liability;
    }

    public void setLiability(String liability) {
        this.liability = liability;
    }

    public String getMatchedOdds() {
        return matchedOdds;
    }

    public void setMatchedOdds(String matchedOdds) {
        this.matchedOdds = matchedOdds;
    }

    public String getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(String profitLoss) {
        this.profitLoss = profitLoss;
    }
}
