package com.example.dits.controllers;

import com.example.dits.dto.TopicStatisticByTests;
import com.example.dits.service.impl.StatisticServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminStatisticController {

    private final StatisticServiceImpl statisticService;

    @GetMapping("/statistic")
    public String testStatistic(){
        return "admin/testStatistics";
    }

    @ModelAttribute("statistic")
    private List<TopicStatisticByTests> getTopicStatisticByTests(){
        return  statisticService.getListTopicStaticByTests();
    }
}
