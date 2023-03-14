package com.gestao.trade.model.specification;

import com.gestao.trade.controller.params.BetQueryParams;
import com.gestao.trade.model.Bet;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BetSpecification implements Specification<Bet> {
    private final String championship;
    private final String dayOfWeek;
    private final String backOrLay;
    private final String method;
    private final String market;
    private final Float roiMin;
    private final Float roiMax;
    private final Float oddMin;
    private final Float oddMax;
    private final Float netProfitMin;
    private final Float netProfitMax;
    private final String team;

    public BetSpecification(BetQueryParams queryParams) {
        this.championship = queryParams.getChampionship();
        this.dayOfWeek = queryParams.getDayOfWeek();
        this.backOrLay = queryParams.getBackOrLay();
        this.method = queryParams.getMethod();
        this.market = queryParams.getMarket();
        this.roiMin = queryParams.getRoiMin();
        this.roiMax = queryParams.getRoiMax();
        this.oddMin = queryParams.getOddMin();
        this.oddMax = queryParams.getOddMax();
        this.netProfitMin = queryParams.getNetProfitMin();
        this.netProfitMax = queryParams.getNetProfitMax();
        this.team = queryParams.getTeam();
    }


    @Override
    public Predicate toPredicate(Root<Bet> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        if (dayOfWeek != null && !dayOfWeek.isEmpty()) {
            predicates.add(builder.like(builder.lower(root.get("dayOfWeek")), "%" + dayOfWeek.toLowerCase() + "%"));
        }

        if (championship != null && !championship.isEmpty()) {
            predicates.add(builder.like(builder.lower(root.get("championship")), "%" + championship.toLowerCase() + "%"));
        }

        if (backOrLay != null && !backOrLay.isEmpty()) {
            predicates.add(builder.like(builder.lower(root.get("backOrLay")), "%" + backOrLay.toLowerCase() + "%"));
        }

        if (method != null && !method.isEmpty()) {
            predicates.add(builder.like(builder.lower(root.get("method")), "%" + method.toLowerCase() + "%"));
        }

        if (market != null && !market.isEmpty()) {
            predicates.add(builder.like(builder.lower(root.get("market")), "%" + market.toLowerCase() + "%"));
        }

        if (roiMin != null && roiMax != null) {
            predicates.add(builder.between(root.get("roi"), roiMin, roiMax));
        } else if (roiMin != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("roi"), roiMin));
        } else if (roiMax != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("roi"), roiMax));
        }

        if (oddMin != null && oddMax != null) {
            predicates.add(builder.between(root.get("roi"), oddMin, oddMax));
        } else if (oddMin != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("roi"), oddMin));
        } else if (oddMax != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("roi"), oddMax));
        }

        if (netProfitMin != null && netProfitMax != null) {
            predicates.add(builder.between(root.get("roi"), netProfitMin, netProfitMax));
        } else if (netProfitMin != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("roi"), netProfitMin));
        } else if (netProfitMax != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("roi"), netProfitMax));
        }

        if (team != null && !team.isEmpty()) {
            String teamLike = "%" + team.toLowerCase() + "%";
            predicates.add(builder.or(
                    builder.like(builder.lower(root.get("homeTeam")), teamLike),
                    builder.like(builder.lower(root.get("awayTeam")), teamLike)
            ));
        }

        if (predicates.isEmpty()) {
            return builder.conjunction();
        } else {
            return builder.and(predicates.toArray(new Predicate[0]));
        }
    }
}