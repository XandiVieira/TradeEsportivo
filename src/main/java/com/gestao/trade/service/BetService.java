package com.gestao.trade.service;

import com.gestao.trade.model.Bet;
import com.gestao.trade.model.dto.BetDtoRequest;
import com.gestao.trade.model.dto.BetDtoResponse;
import com.gestao.trade.repository.BetRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BetService {

    @Autowired
    private BetRepository betRepository;

    public List<BetDtoResponse> uploadHistory(InputStream is) {
        List<Bet> bets = betRepository.saveAll(readCSV(is));
        return bets.stream().map(bet -> {
            return BetDtoResponse.builder()
                    .date(bet.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                    .dayOfWeek(bet.getDayOfWeek())
                    .awayTeam(bet.getAwayTeam())
                    .homeTeam(bet.getHomeTeam())
                    .backOrLay(bet.getBackOrLay())
                    .method(bet.getMethod())
                    .market(bet.getMarket())
                    .betDescription(bet.getSelection())
                    .odd(bet.getOdd())
                    .investment(bet.getLiability())
                    .grossProfit(bet.getGrossProfit())
                    .netProfit(bet.getGrossProfit().subtract((bet.getGrossProfit().multiply(new BigDecimal("6.5").divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)))))
                    .returnOverInvestment(bet.getGrossProfit().divide(bet.getLiability(), 2, RoundingMode.HALF_UP).floatValue()).build();
        }).toList();
    }

    private List<Bet> readCSV(InputStream is) {

        List<BetDtoRequest> dtos = new ArrayList<>();

        Reader reader = null; // specify the correct character encoding
        try {
            reader = new InputStreamReader(is, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        CSVReader csvReader = new CSVReader(reader);

        try {
            csvReader.skip(1); // skip the header row
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        List<String[]> rows = null;
        try {
            rows = csvReader.readAll();
        } catch (IOException | CsvException ex) {
            throw new RuntimeException(ex);
        }
        for (String[] row : rows) {
            BetDtoRequest dto = new BetDtoRequest();
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

        dtos.forEach(betDtoRequest -> {

            Long id = null;
            LocalDate date = null;
            String dayOfWeek = null;
            String homeTeam = null;
            String awayTeam = null;
            String backOrLay = null;
            String market = null;
            String selection = null;
            Float odd = null;
            BigDecimal liability = null;
            BigDecimal grossProfit = null;

            if (betDtoRequest.getBetId() != null && !betDtoRequest.getBetId().isEmpty()) {
                id = Long.valueOf(betDtoRequest.getBetId());
            }

            if (betDtoRequest.getMarket() != null && !betDtoRequest.getMarket().isEmpty()) {
                String[] parts = betDtoRequest.getMarket().split("/");

                String[] teams = parts[1].split(" x ");

                homeTeam = teams[0].trim();
                awayTeam = teams[1].trim();

                market = parts[2].trim();
                if (parts.length > 3) {
                    market = market + parts[3];
                }
            }

            if (betDtoRequest.getBetPlaced() != null && !betDtoRequest.getBetPlaced().isEmpty()) {
                date = convertDate(betDtoRequest.getBetPlaced());
                dayOfWeek = translateDayOfWeek(date.getDayOfWeek().name());
            }

            if (betDtoRequest.getBidType() != null && !betDtoRequest.getBidType().isEmpty()) {
                if (betDtoRequest.getBidType().equals("Contra")) {
                    backOrLay = "L";
                } else {
                    backOrLay = "B";
                }
            }

            if (betDtoRequest.getSelection() != null && !betDtoRequest.getSelection().isEmpty()) {
                selection = betDtoRequest.getSelection();
            }

            if (betDtoRequest.getProfitLoss() != null && !betDtoRequest.getProfitLoss().isEmpty()) {
                if (betDtoRequest.getProfitLoss().contains("(")) {
                    grossProfit = new BigDecimal(betDtoRequest.getProfitLoss().replace("(", "").replace(")", "")).negate();
                } else {
                    grossProfit = new BigDecimal(betDtoRequest.getProfitLoss().replace("(", "").replace(")", ""));
                }
            }

            if (betDtoRequest.getLiability() != null && !betDtoRequest.getLiability().isEmpty()) {
                liability = new BigDecimal(betDtoRequest.getLiability());
            } else if (betDtoRequest.getStopLoss() != null && !betDtoRequest.getStopLoss().isEmpty()) {
                liability = new BigDecimal(betDtoRequest.getStopLoss());
            }

            if (betDtoRequest.getMatchedOdds() != null && !betDtoRequest.getMatchedOdds().isEmpty()) {
                odd = Float.valueOf(betDtoRequest.getMatchedOdds());
            }
            bets.add(new Bet(id, date, dayOfWeek, homeTeam, awayTeam, backOrLay, market, selection, odd, liability, grossProfit));
        });
        return bets;
    }

    public static String translateDayOfWeek(String dayOfWeek) {
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

    private static LocalDate convertDate(String input) {

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

        // Parse the input string into a LocalDate object
        String[] onlyDate = input.split(" ");

        // Define the formatter for the input string
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yy");

        // Parse the input string into a LocalDate object
        LocalDate date = LocalDate.parse(onlyDate[0], inputFormatter);

        // Convert the year to the 4-digit format
        if (date.getYear() < 100) {
            date = date.plusYears(100);
        }

        return date;
    }
}
