package com.example.dits.controllers;

import com.example.dits.dto.*;
import com.example.dits.entity.Topic;
import com.example.dits.entity.User;
import com.example.dits.mapper.TestStatisticMapper;
import com.example.dits.service.TopicService;
import com.example.dits.service.UserService;
import com.example.dits.service.impl.StatisticServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AdminStatisticController {

    private final ModelMapper modelMapper;
    private final StatisticServiceImpl statisticService;
    private final TopicService topicService;
    private final UserService userService;
    private final TestStatisticMapper statisticMapper;

    @GetMapping("/adminStatistic")
    public String testStatistic(ModelMap model){
        List<TopicDTO> topicDTOList = topicService.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
        model.addAttribute("topicList",topicDTOList);
        return "admin/test-statistic";
    }

    @ResponseBody
    @GetMapping("/getTestsStatistic")
    public List<TestStatistic> getTestsStatistics(@RequestParam int id) {
        return statisticService.getListOfTestsWithStatisticsByTopic(id);
    }

    @GetMapping("/getUserStatistic")
    public String userStatistic(ModelMap model){
        List<SimpleUserDTO> userDTOS = userService.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
        model.addAttribute("userListStat",userDTOS);
        return "admin/user-statistic";
    }

    @ResponseBody
    @GetMapping("/getUserTestsStatistic")
    public List<TestStatisticByUser> getUserStatistics(@RequestParam int id) {
        User user = userService.getUserByUserId(id);
        UserStatistics userStatistics = statisticService.getUserStatistics(user);
        return statisticMapper.convertToUserStatisticDTO(userStatistics);
    }

    private TopicDTO convertToDTO(Topic topic){
        return modelMapper.map(topic, TopicDTO.class);
    }

    private SimpleUserDTO convertToDTO(User user){
        return modelMapper.map(user, SimpleUserDTO.class);
    }
}
