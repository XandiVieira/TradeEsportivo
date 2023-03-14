package com.gestao.trade.mapper;

import com.gestao.trade.model.Bet;
import com.gestao.trade.model.dto.BetResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BetModelMapper {

    //@Value("${commission.rate}")
    static BigDecimal COMISSION = new BigDecimal("6.51");
    private static final ModelMapper modelMapper = new ModelMapper();

    public static BetResponseDto mapBetToBetDtoResponse(Bet bet) {
        BetResponseDto response = modelMapper.map(bet, BetResponseDto.class);

        response.setDate(convertTimestampToString(bet.getDate()));
        response.setDayOfWeek(getDayOfWeekName(bet.getDate()));
        response.setNetProfit(bet.getGrossProfit().subtract(bet.getGrossProfit().multiply(COMISSION.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP))).setScale(2, RoundingMode.HALF_UP));
        response.setReturnOverInvestment(response.getNetProfit().divide(bet.getLiability(), 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP).floatValue() + "0%");
        return response;
    }

    private static String getDayOfWeekName(Long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, java.time.ZoneOffset.UTC);
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        String displayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
        return displayName.substring(0, 1).toUpperCase() + displayName.substring(1);
    }

    private static String convertTimestampToString(Long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return dateFormat.format(new Date(timestamp));
    }

    private static String translateDayOfWeek(String dayOfWeek) {
        return switch (dayOfWeek.toUpperCase()) {
            case "SUNDAY" -> "Domingo";
            case "MONDAY" -> "Segunda-feira";
            case "TUESDAY" -> "Terça-feira";
            case "WEDNESDAY" -> "Quarta-feira";
            case "THURSDAY" -> "Quinta-feira";
            case "FRIDAY" -> "Sexta-feira";
            case "SATURDAY" -> "Sábado";
            default -> throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);
        };
    }

    public static List<BetResponseDto> mapBetListToBetResponseDtoList(List<Bet> bets) {
        List<BetResponseDto> betResponseDtoList = new ArrayList<>();
        bets.forEach(bet -> betResponseDtoList.add(mapBetToBetDtoResponse(bet)));
        return betResponseDtoList;
    }
}