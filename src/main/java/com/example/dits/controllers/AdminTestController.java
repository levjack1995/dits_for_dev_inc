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
    public QuestionWithAnswersDTO getQuestionsWithAnswers(@RequestParam int id){
        Question question = questionService.getQuestionById(id);
        questionMapper.convertToQuestionWithAnswersDTO(question);
        return questionMapper.convertToQuestionWithAnswersDTO(question);
    }

    @ResponseBody
    @PostMapping("/removeTest")
    public List<TestWithQuestionsDTO> removeTest(@RequestParam int testId, @RequestParam int topicId){
        testService.removeTestByTestId(testId);
        return getTestWithQuestionsDTOList(topicService.getTopicByTopicId(topicId));
    }

    @ResponseBody
    @PostMapping("/removeTopic")
    public List<TopicDTO> removeTopic(@RequestParam int topicId){
        topicService.removeTopicByTopicId(topicId);
        return getTopicDTOList();
    }

    private List<TopicDTO> getTopicDTOList() {
        return topicService.findAll().stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @ResponseBody
    @PostMapping("/addTopic")
    public List<TopicDTO> addTopic(@RequestParam String name){
        Topic topic = new Topic(name);
        topicService.save(topic);
        return getTopicDTOList();
    }

    @ResponseBody
    @PostMapping("/addTest")
    public List<TestWithQuestionsDTO> addTest(@RequestParam String name, @RequestParam String description,
                                     @RequestParam int topicId){
        Topic topic = topicService.getTopicByTopicId(topicId);
        Test test = Test.builder().name(name).description(description).topic(topic).build();
        testService.save(test);
        return getTestWithQuestionsDTOList(topic);
    }

    @ResponseBody
    @PostMapping("/editTopic")
    public List<TopicDTO> editTopic(@RequestParam int id, @RequestParam String name){
        topicService.updateTopicName(id,name);
        return getTopicDTOList();
    }

    @ResponseBody
    @PostMapping("/editTest")
    public List<TestWithQuestionsDTO> editTest(@RequestParam String name, @RequestParam String description,
                                               @RequestParam int testId, @RequestParam int topicId){
        testService.update(testId,name,description);
        return getTestWithQuestionsDTOList(topicService.getTopicByTopicId(topicId));
    }

    @ResponseBody
    @PostMapping("/addQuestion")
    public List<TestWithQuestionsDTO> addQuestion(@RequestParam String description,@RequestParam int testId
            , @RequestParam int topicId){
        questionService.addQuestion(description,testId);
        return getTestWithQuestionsDTOList(topicService.getTopicByTopicId(topicId));
    }

    @ResponseBody
    @PostMapping("/editQuestion")
    public List<TestWithQuestionsDTO> editQuestion(@RequestParam String description, @RequestParam int questionId,
                                                   @RequestParam int topicId){
        questionService.editQuestion(description,questionId);
        return getTestWithQuestionsDTOList(topicService.getTopicByTopicId(topicId));
    }


    private List<TestWithQuestionsDTO> getTestWithQuestionsDTOList(Topic topic) {
        return topic.getTestList().stream().map(testMapper::convertToTestDTO).collect(Collectors.toList());
    }

    private TopicDTO convertToDTO(Topic topic){
        return modelMapper.map(topic, TopicDTO.class);
    }

}
