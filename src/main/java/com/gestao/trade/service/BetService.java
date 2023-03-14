package com.gestao.trade.service;

import com.gestao.trade.controller.params.BetQueryParams;
import com.gestao.trade.mapper.BetModelMapper;
import com.gestao.trade.model.Bet;
import com.gestao.trade.model.dto.BetRequestDto;
import com.gestao.trade.model.dto.BetResponseDto;
import com.gestao.trade.model.specification.BetSpecification;
import com.gestao.trade.repository.BetRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class BetService {

    @Autowired
    private BetRepository betRepository;

    public Page<BetResponseDto> uploadHistory(InputStream is) {
        List<Bet> bets = betRepository.saveAll(BetModelMapper.mapBetRequestDtoListToBetList(readCSV(is)));
        return BetModelMapper.mapBetListToBetResponseDtoList(new PageImpl<>(bets));
    }

    private List<BetRequestDto> readCSV(InputStream is) {

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

        return dtos;
    }

    public Page<BetResponseDto> findBets(BetQueryParams queryParams) {
        Sort sort = Sort.by(queryParams.getSortDirection().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, queryParams.getSortBy());
        Pageable pageable = PageRequest.of(queryParams.getPageNumber(), queryParams.getPageSize(), sort);
        Specification<Bet> spec = new BetSpecification(queryParams);
        return BetModelMapper.mapBetListToBetResponseDtoList(betRepository.findAll(spec, pageable));
    }
}