package com.example.dits.controllers;

import com.example.dits.entity.Test;
import com.example.dits.entity.Topic;
import com.example.dits.service.TestService;
import com.example.dits.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UserHomeController {

    private final TestService testService;
    private final TopicService topicService;



    @GetMapping("/chooseTheme")
    @ResponseBody
    public List<String> chooseTheme(@RequestParam(value = "topic", required = false)String topicName, HttpSession session){
        Topic topic = topicService.getTopicByName(topicName);
        List<Test> tests = testService.getTestsByTopic(topic);
        session.setAttribute("tests", tests);
        if(tests.isEmpty()){
            return new ArrayList<String>();
        }
        else {
            List<String> testsName = tests.stream().map(Test::getName).collect(Collectors.toList());
            return testsName;
        }
    }
    @GetMapping("/getDescription")
    @ResponseBody
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
}
