package com.gestao.trade.controller;

import com.gestao.trade.model.dto.BetResponseDto;
import com.gestao.trade.service.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/bets")
public class BetController {

    @Autowired
    private BetService betService;

    @PostMapping("/uploadHistory")
    public ResponseEntity<List<BetResponseDto>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            List<BetResponseDto> result = betService.uploadHistory(is);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<BetResponseDto>> getBets(@RequestParam(name = "orderBy", defaultValue = "date") String orderBy) {
        return ResponseEntity.status(HttpStatus.OK).body(betService.getBets(orderBy));
    }

}
