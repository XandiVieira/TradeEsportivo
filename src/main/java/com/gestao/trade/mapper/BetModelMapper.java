package com.gestao.trade.mapper;

import com.gestao.trade.model.Bet;
import com.gestao.trade.model.dto.BetResponseDto;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BetModelMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static BetResponseDto mapBetToBetDtoResponse(Bet bet) {
        BetResponseDto response = modelMapper.map(bet, BetResponseDto.class);
        response.setDate(bet.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        response.setNetProfit(bet.getGrossProfit().subtract((bet.getGrossProfit().multiply(new BigDecimal("6.5").divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)))));
        response.setReturnOverInvestment(bet.getGrossProfit().divide(bet.getLiability(), 2, RoundingMode.HALF_UP).floatValue());
        return response;
    }

    public static List<BetResponseDto> mapBetListToBetResponseDtoList(List<Bet> bets) {
        List<BetResponseDto> betResponseDtoList = new ArrayList<>();
        bets.forEach(bet -> betResponseDtoList.add(mapBetToBetDtoResponse(bet)));
        return betResponseDtoList;
    }
}