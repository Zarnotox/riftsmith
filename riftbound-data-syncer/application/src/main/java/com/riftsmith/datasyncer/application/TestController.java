package com.riftsmith.datasyncer.application;

import com.riftsmith.datasyncer.domain.datafetch.DataFetcher;
import com.riftsmith.datasyncer.domain.riftbound.CardEntity;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
@RequestMapping("api/test")
@AllArgsConstructor
public class TestController {
    private DataFetcher dataFetcher;

    @GetMapping("/cards")
    public Stream<CardEntity> getCards(){
        return dataFetcher.fetchData();
    }
}
