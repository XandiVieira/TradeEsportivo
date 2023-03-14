package com.gestao.trade.repository;

import com.gestao.trade.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {

    List<Bet> findAllByOrderByDateDesc();
    List<Bet> findAllByOrderByGrossProfitDesc();
}