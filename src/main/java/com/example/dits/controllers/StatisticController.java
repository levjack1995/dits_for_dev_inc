package com.example.dits.controllers;

import com.example.dits.dto.UserStatistics;
import com.example.dits.entity.User;
import com.example.dits.facades.StatisticFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticFacade statisticFacade;

    @GetMapping("/personalStatistic")
    public String personalStatistic(ModelMap model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        UserStatistics userStatistics = statisticFacade.getUserStatistics(user);
        model.addAttribute("statisticList",userStatistics.getTestStatisticList());
        return "user/personalStatistic";
    }
}
