package com.gestao.trade.service;

import com.gestao.trade.mapper.BetModelMapper;
import com.gestao.trade.model.Bet;
import com.gestao.trade.model.dto.BetRequestDto;
import com.gestao.trade.model.dto.BetResponseDto;
import com.gestao.trade.repository.BetRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BetService {

    @Autowired
    private BetRepository betRepository;

    public List<BetResponseDto> uploadHistory(InputStream is) {
        List<Bet> bets = betRepository.saveAll(readCSV(is));
        return BetModelMapper.mapBetListToBetResponseDtoList(bets);
    }

    private List<Bet> readCSV(InputStream is) {

        List<BetRequestDto> dtos = new ArrayList<>();

        Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
        CSVReader csvReader = new CSVReader(reader);

        try {
            csvReader.skip(1); // skip the header row
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        List<String[]> rows;
        try {
            rows = csvReader.readAll();
        } catch (IOException | CsvException ex) {
            throw new RuntimeException(ex);
        }
        for (String[] row : rows) {
            BetRequestDto dto = new BetRequestDto();
            dto.setMarket(row[0]);
            dto.setSelection(row[1]);
            dto.setBidType(row[2]);
            dto.setBetId(row[3]);
            dto.setBetPlaced(row[4]);
            dto.setPersistence(row[6]);
            dto.setRequiredOdds(row[7]);
            dto.setStopLoss(row[8]);
            dto.setLiability(row[9]);
            dto.setMatchedOdds(row[10]);
            dto.setProfitLoss(row[11]);

            dtos.add(dto);
        }

        List<Bet> bets = new ArrayList<>();

        dtos.forEach(betRequestDto -> {

            Long id = null;
            Long date = null;
            String homeTeam = null;
            String awayTeam = null;
            String backOrLay = null;
            String market = null;
            String selection = null;
            Float odd = null;
            BigDecimal liability = null;
            BigDecimal grossProfit = null;

            if (betRequestDto.getBetId() != null && !betRequestDto.getBetId().isEmpty()) {
                id = Long.valueOf(betRequestDto.getBetId());
            }

            if (betRequestDto.getMarket() != null && !betRequestDto.getMarket().isEmpty()) {
                String[] parts = betRequestDto.getMarket().split("/");

                String[] teams = parts[1].split(" x ");

                homeTeam = teams[0].trim();
                awayTeam = teams[1].trim();

                market = parts[2].trim();
                if (parts.length > 3) {
                    market = market + parts[3];
                }
            }

            if (betRequestDto.getBetPlaced() != null && !betRequestDto.getBetPlaced().isEmpty()) {
                date = convertDate(betRequestDto.getBetPlaced());
            }

            if (betRequestDto.getBidType() != null && !betRequestDto.getBidType().isEmpty()) {
                if (betRequestDto.getBidType().equals("Contra")) {
                    backOrLay = "L";
                } else {
                    backOrLay = "B";
                }
            }

            if (betRequestDto.getSelection() != null && !betRequestDto.getSelection().isEmpty()) {
                selection = betRequestDto.getSelection();
            }

            if (betRequestDto.getProfitLoss() != null && !betRequestDto.getProfitLoss().isEmpty()) {
                if (betRequestDto.getProfitLoss().contains("(")) {
                    grossProfit = new BigDecimal(betRequestDto.getProfitLoss().replace("(", "").replace(")", "")).negate();
                } else {
                    grossProfit = new BigDecimal(betRequestDto.getProfitLoss().replace("(", "").replace(")", ""));
                }
            }

            if (betRequestDto.getLiability() != null && !betRequestDto.getLiability().isEmpty()) {
                liability = new BigDecimal(betRequestDto.getLiability());
            } else if (betRequestDto.getStopLoss() != null && !betRequestDto.getStopLoss().isEmpty()) {
                liability = new BigDecimal(betRequestDto.getStopLoss());
            }

            if (betRequestDto.getMatchedOdds() != null && !betRequestDto.getMatchedOdds().isEmpty()) {
                odd = Float.valueOf(betRequestDto.getMatchedOdds());
            }
            bets.add(new Bet(id, date, homeTeam, awayTeam, backOrLay, market, selection, odd, liability, grossProfit));
        });
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

    public List<BetResponseDto> getBets(String orderBy) {
        if (orderBy.equals("grossProfit")) {
            return BetModelMapper.mapBetListToBetResponseDtoList(betRepository.findAllByOrderByGrossProfitDesc());
        } else {
            return BetModelMapper.mapBetListToBetResponseDtoList(betRepository.findAllByOrderByDateDesc());
        }
    }
}