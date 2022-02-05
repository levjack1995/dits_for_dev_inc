package com.example.dits.controllers;


import com.example.dits.dto.TestWithQuestionsDTO;
import com.example.dits.dto.TopicDTO;
import com.example.dits.entity.Role;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/testBuilder")
public class AdminTestController {
    private final ModelMapper modelMapper;
    private final TopicService topicService;
    private final TestService testService;

    @GetMapping("")
    public String getTopics(ModelMap model) {
        List<Topic> topicList = topicService.findAll();
        List<TopicDTO> topicDTOList = topicList.stream().map(this::convertToDTO).collect(Collectors.toList());
        model.addAttribute("topicList",topicDTOList);
        return "admin/admin4";
    }

    @PostMapping("")
    public List<TestWithQuestionsDTO> getTestsWithQuestions(@RequestParam int ID,ModelMap modelMap) {
        List<Test> testList = testService.getTestsByTopic_TopicId(ID);
        List<TestWithQuestionsDTO> testWithQuestionsDTOList = testList.stream().map(this::convertToDTO).collect(Collectors.toList());
        return testWithQuestionsDTOList;
    }

    @GetMapping("/removeTest")
    public String removeTest(@RequestParam int testId){
        Test test = testService.getTestByTestId(testId);
        testService.delete(test);
        return "redirect:/admin4";
    }

    @GetMapping("/removeTopic")
    public String removeTopic(@RequestParam String topicId){
        Topic topic = topicService.getTopicByName(topicId);
        topicService.delete(topic);
        return "redirect:/admin4";
    }

    @GetMapping("/addTopic")
    public String removeUser(@RequestParam String name,@RequestParam String description){
        Topic topic = new Topic(name,description);
        topicService.save(topic);
        return "redirect:/admin4";
    }




    private TopicDTO convertToDTO(Topic topic){
        return modelMapper.map(topic, TopicDTO.class);
    }

    private TestWithQuestionsDTO convertToDTO(Test test){
        return modelMapper.map(test, TestWithQuestionsDTO.class);
    }

}
