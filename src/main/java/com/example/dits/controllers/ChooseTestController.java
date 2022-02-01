package com.example.dits.controllers;

import com.example.dits.dto.TestInfoDTO;
import com.example.dits.entity.Test;
import com.example.dits.service.TestService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ChooseTestController {

    private final TestService testService;
    private final ModelMapper modelMapper;

    @GetMapping("/chooseTheme")
    public List<TestInfoDTO> chooseTheme(@RequestParam(value = "topic", required = false)String topicName, HttpSession session){
        List<Test> tests = testService.getTestsByTopicName(topicName);
        session.setAttribute("tests", tests);
        if(tests.isEmpty()){
            return null;
        }
        else {
            return tests.stream().map(this::convertToDTO).collect(Collectors.toList());
        }
    }

    @GetMapping("/getDescription")
    public String description(@RequestParam(value = "test", required = false)String test, HttpSession session){
          List<Test> tests  = (List<Test>) session.getAttribute("tests");
          Optional<Test> optTest = tests.stream().filter(x->x.getName().equals(test)).findAny();
        if(optTest.isEmpty()){
            return "[]";
        }
        else {
            return "[\"" + optTest.get().getDescription() +  "\"]";
        }
    }

    private TestInfoDTO convertToDTO(Test test){
        return modelMapper.map(test, TestInfoDTO.class);
    }
}
