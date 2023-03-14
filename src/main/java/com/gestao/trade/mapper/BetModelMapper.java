package com.gestao.trade.mapper;

import com.gestao.trade.model.Bet;
import com.gestao.trade.model.dto.BetRequestDto;
import com.gestao.trade.model.dto.BetResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
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
        return response;
    }

    public static Page<BetResponseDto> mapBetListToBetResponseDtoList(Page<Bet> bets) {
        List<BetResponseDto> betResponseDtoList = new ArrayList<>();
        bets.forEach(bet -> betResponseDtoList.add(mapBetToBetDtoResponse(bet)));
        return new PageImpl<>(betResponseDtoList);
    }

    public static List<Bet> mapBetRequestDtoListToBetList(List<BetRequestDto> dtos) {

        List<Bet> bets = new ArrayList<>();

        for (BetRequestDto dto : dtos) {

            Long id = dto.getBetId() != null && !dto.getBetId().isEmpty() ? Long.valueOf(dto.getBetId()) : null;
            String dayOfWeek = dto.getBetPlaced() != null && !dto.getBetPlaced().isEmpty() ? getDayOfWeekName(convertDate(dto.getBetPlaced())) : null;
            Long date = dto.getBetPlaced() != null && !dto.getBetPlaced().isEmpty() ? convertDate(dto.getBetPlaced()) : null;
            String homeTeam = null;
            String awayTeam = null;
            String market = null;
            String selection = dto.getSelection();
            Float odd = dto.getMatchedOdds() != null && !dto.getMatchedOdds().isEmpty() ? Float.valueOf(dto.getMatchedOdds()) : null;
            BigDecimal liability = dto.getLiability() != null && !dto.getLiability().isEmpty() ? new BigDecimal(dto.getLiability()) : null;
            BigDecimal netProfit = null;
            BigDecimal grossProfit = null;
            Float roi = null;
            String backOrLay = null;

            if (dto.getMarket() != null && !dto.getMarket().isEmpty()) {
                String[] parts = dto.getMarket().split("/");
                String[] teams = parts[1].split(" x ");
                homeTeam = teams[0].trim();
                awayTeam = teams[1].trim();
                market = parts[2].trim();
                if (parts.length > 3) {
                    market = market + parts[3];
                }
            }

            if (dto.getBidType() != null && !dto.getBidType().isEmpty()) {
                if (dto.getBidType().equals("Contra")) {
                    backOrLay = "L";
                } else {
                    backOrLay = "B";
                }
            }

            if (dto.getLiability() == null || dto.getLiability().isEmpty()) {
                liability = dto.getStopLoss() != null && !dto.getStopLoss().isEmpty() ? new BigDecimal(dto.getStopLoss()) : null;
            }

            if (dto.getProfitLoss() != null && !dto.getProfitLoss().isEmpty()) {
                grossProfit = new BigDecimal(dto.getProfitLoss().replace("(", "").replace(")", ""));
                if (dto.getProfitLoss().contains("(")) {
                    grossProfit = grossProfit.negate();
                }
                if (grossProfit.intValue() > 0) {
                    netProfit = grossProfit.subtract(grossProfit.multiply(new BigDecimal(String.valueOf(COMISSION)).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP))).setScale(2, RoundingMode.HALF_UP);
                } else {
                    netProfit = grossProfit;
                }
                if (liability != null) {
                    roi = netProfit.divide(liability, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP).floatValue();
                }
            }

            bets.add(new Bet(id, dayOfWeek, date, homeTeam, awayTeam, backOrLay, market, selection, odd, liability, grossProfit, netProfit, roi));
        }
        return bets;
    }


    private static Long convertDate(String input) {

        if (input.contains("jan")) {
            input = input.replace("jan", "01");
        } else if (input.contains("fev")) {
            input = input.replace("fev", "02");
        } else if (input.contains("mar")) {
            input = input.replace("mar", "03");
        } else if (input.contains("abr")) {
            input = input.replace("abr", "04");
        } else if (input.contains("mai")) {
            input = input.replace("mai", "05");
        } else if (input.contains("jun")) {
            input = input.replace("jun", "06");
        } else if (input.contains("jul")) {
            input = input.replace("jul", "07");
        } else if (input.contains("ago")) {
            input = input.replace("ago", "08");
        } else if (input.contains("set")) {
            input = input.replace("set", "09");
        } else if (input.contains("out")) {
            input = input.replace("out", "10");
        } else if (input.contains("nov")) {
            input = input.replace("nov", "11");
        } else if (input.contains("dez")) {
            input = input.replace("dez", "12");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
        Date date;
        try {
            date = dateFormat.parse(input);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return date.getTime();
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
}