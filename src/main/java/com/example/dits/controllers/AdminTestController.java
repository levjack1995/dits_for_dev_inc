package com.example.dits.controllers;


import com.example.dits.dto.QuestionWithAnswersDTO;
import com.example.dits.dto.TestWithQuestionsDTO;
import com.example.dits.dto.TopicDTO;
import com.example.dits.entity.Question;
import com.example.dits.entity.Test;
import com.example.dits.entity.Topic;
import com.example.dits.mapper.QuestionMapper;
import com.example.dits.mapper.TestMapper;
import com.example.dits.service.QuestionService;
import com.example.dits.service.TestService;
import com.example.dits.service.TopicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AdminTestController {
    private final ModelMapper modelMapper;
    private final TopicService topicService;
    private final TestService testService;
    private final QuestionService questionService;
    private final ObjectMapper mapper;
    private final TestMapper testMapper;
    private final QuestionMapper questionMapper;


    @GetMapping("/testBuilder")
    public String getTopics(ModelMap model) {
        List<Topic> topicList = topicService.findAll();
        List<TopicDTO> topicDTOList = topicList.stream().map(this::convertToDTO).collect(Collectors.toList());
        model.addAttribute("topicList",topicDTOList);
        return "/admin/test-editor";
    }

    @ResponseBody
    @GetMapping("/getTests")
    public List<TestWithQuestionsDTO> getTestsWithQuestions(@RequestParam int id) {
        List<Test> testList = testService.getTestsByTopic_TopicId(id);
        return testList.stream().map(testMapper::convertToTestDTO).collect(Collectors.toList());
    }

    @ResponseBody
    @GetMapping("/getAnswers")
    public List<QuestionWithAnswersDTO> getQuestionsWithAnswers(@RequestParam int id){
        List<Question> questions = questionService.getQuestionsByTest_TestId(id);
        return questions.stream().map(questionMapper::convertToQuestionWithAnswersDTO).collect(Collectors.toList());
    }

    @GetMapping("/removeTest")
    public String removeTest(@RequestParam int testId){
        Test test = testService.getTestByTestId(testId);
        testService.delete(test);
        return "redirect:/admin/index";
    }

    @GetMapping("/removeTopic")
    public String removeTopic(@RequestParam String topicId){
        Topic topic = topicService.getTopicByName(topicId);
        topicService.delete(topic);
        return "redirect:/admin/index";
    }

    @GetMapping("/addTopic")
    public String removeUser(@RequestParam String name,@RequestParam String description){
        Topic topic = new Topic(name,description);
        topicService.save(topic);
        return "redirect:/admin/index";
    }




    private TopicDTO convertToDTO(Topic topic){
        return modelMapper.map(topic, TopicDTO.class);
    }


}
