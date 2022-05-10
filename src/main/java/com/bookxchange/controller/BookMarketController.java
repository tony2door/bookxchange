package com.bookxchange.controller;

import com.bookxchange.model.BookMarketEntity;
import com.bookxchange.service.BookMarketService;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bookMarket")
public class BookMarketController {

    @Autowired
    BookMarketService bookMarketService;

    @GetMapping("/getByIsbn")
    public ResponseEntity<List<BookMarketEntity>> getAllByIsbn(@RequestParam String isbn) {

        List<BookMarketEntity> bookMarketEntities = bookMarketService.findAllByIsbn(isbn);

        return new ResponseEntity<List<BookMarketEntity>>(bookMarketEntities, HttpStatus.OK);
    }


    @GetMapping("/getByUserId")
    public ResponseEntity<List<BookMarketEntity>> getAllByUserId(@RequestParam String userUuid) {

        List<BookMarketEntity> bookMarketEntities = bookMarketService.findAllByUserId(userUuid);

        return new ResponseEntity<List<BookMarketEntity>>(bookMarketEntities, HttpStatus.OK);
    }
}