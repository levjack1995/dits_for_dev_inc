package com.example.dits.controllers;

import com.example.dits.dto.TestInfoDTO;
import com.example.dits.dto.TopicDTO;
import com.example.dits.dto.UserStatistics;
import com.example.dits.entity.Test;
import com.example.dits.entity.Topic;
import com.example.dits.entity.User;
import com.example.dits.service.TestService;
import com.example.dits.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AdminTestController {
    private final ModelMapper modelMapper;
    private final TopicService topicService;
    private final TestService testService;

//    @GetMapping("/testBuilder")
//    public String personalStatistic(ModelMap model) {
//        List<Topic> topicList = topicService.findAll();
//        List<TopicDTO> topicDTOList = topicList.stream().map(this::convertToDTO).collect(Collectors.toList());
//        model.addAttribute("topicList",topicDTOList);
//        return "admin/admin4";
//    }

    @GetMapping("/testBuilder")
    public List<TestInfoDTO> personalStatistic(@RequestParam int ID) {
        List<Test> testList = testService.getTestsByTopic_TopicId(ID);
        List<TestInfoDTO> testInfoDTOList = testList.stream().map(this::convertToDTO).collect(Collectors.toList());
        return testInfoDTOList;
    }


    private TopicDTO convertToDTO(Topic topic){
        return modelMapper.map(topic, TopicDTO.class);
    }

    private TestInfoDTO convertToDTO(Test test){
        return modelMapper.map(test, TestInfoDTO.class);
    }

}
