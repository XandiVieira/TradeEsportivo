package com.gestao.trade.controller;

import com.gestao.trade.controller.params.BetQueryParams;
import com.gestao.trade.model.dto.BetResponseDto;
import com.gestao.trade.service.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/bets")
public class BetController {

    @Autowired
    private BetService betService;

    @PostMapping("/uploadHistory")
    public ResponseEntity<Page<BetResponseDto>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            Page<BetResponseDto> result = betService.uploadHistory(is);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping
    public Page<BetResponseDto> findBets(@ModelAttribute BetQueryParams queryParams) {
        return betService.findBets(queryParams);
    }
}