package com.example.dits.controllers;

import com.example.dits.entity.*;
import com.example.dits.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class TestPageController {

    private final TestService testService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final StatisticService statisticService;

    @GetMapping("/goTest")
    public String goTest(@RequestParam int testId, @RequestParam(value = "theme") String topicName, ModelMap model, HttpSession session){

        Test test = testService.getTestByTestId(testId);
        List<Question> questionList = questionService.getQuestionsByTest(test);
        int quantityOfQuestions = questionList.size();
        int questionNumber = 0;
        int quantityOfRightAnswers = 0;

        List<Answer> answers = answerService.getAnswersFromQuestionList(questionList, questionNumber);
        String questionDescription = questionService.getDescriptionFromQuestionList(questionList, questionNumber);

        session.setAttribute("testName", test.getName());
        session.setAttribute("topicName", topicName);
        session.setAttribute("questionSize", quantityOfQuestions);
        session.setAttribute("quantityOfRightAnswers", quantityOfRightAnswers);
        session.setAttribute("statistics", new ArrayList<Statistic>());
        session.setAttribute("questions",questionList);
        session.setAttribute("questionNumber" , ++questionNumber);

        model.addAttribute("question", questionDescription);
        model.addAttribute("answers", answers);
        return "user/testPage";
    }

    @GetMapping("/nextTestPage")
    public String nextTestPage(@RequestParam(value = "answeredQuestion", required = false) List<Integer> answeredQuestion,
                               ModelMap model,
                               HttpSession session){

        List<Question> questionList = (List<Question>) session.getAttribute("questions");
        int questionNumber = (int) session.getAttribute("questionNumber");
        User user = (User) session.getAttribute("user");
        boolean isCorrect = answerService.isRightAnswer(answeredQuestion,questionList,questionNumber);

        List<Answer> answers = answerService.getAnswersFromQuestionList(questionList, questionNumber);
        String questionDescription = questionService.getDescriptionFromQuestionList(questionList, questionNumber);

        List<Statistic> statisticList = (List<Statistic>) session.getAttribute("statistics");
        statisticList.add(Statistic.builder()
                .question(questionList.get(questionNumber))
                .user(user)
                .correct(isCorrect).build());

        session.setAttribute("statistics", statisticList);
        session.setAttribute("questionNumber" , ++questionNumber);
        model.addAttribute("question",questionDescription);
        model.addAttribute("answers", answers);

        return "user/testPage";
    }

    @GetMapping("/resultPage")
    public String testStatistic(@RequestParam(value = "answeredQuestion", required = false) List<Integer> answeredQuestion,
                                ModelMap model,
                                HttpSession session){


        List<Question> questions = (List<Question>) session.getAttribute("questions");
        int questionNumber = questions.size() - 1;
        boolean isCorrect = answerService.isRightAnswer(answeredQuestion,questions,questionNumber);
        User user = (User) session.getAttribute("user");
        List<Statistic> statisticList = (List<Statistic>) session.getAttribute("statistics");

        if (!isResultPage(questionNumber, statisticList)){
            statisticList.add(Statistic.builder()
                    .question(questions.get(questionNumber))
                    .user(user)
                    .correct(isCorrect).build());
        }

        int countOfRightAnswers = statisticService.calculateRightAnswers(statisticList);
        statisticService.saveStatisticsToDB(statisticList);
        int percentOfRightAnswers = (int) answerService.countPercentsOfRightAnswers(countOfRightAnswers,questions.size());

        model.addAttribute("rightAnswers",countOfRightAnswers);
        model.addAttribute("countOfQuestions", questions.size());
        model.addAttribute("percentageComplete", percentOfRightAnswers);

        return "user/resultPageFinal";
    }

    private boolean isResultPage(int questionNumber, List<Statistic> statisticList) {
        return statisticList.size() >= questionNumber;
    }


}
