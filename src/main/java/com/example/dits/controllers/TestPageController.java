package com.example.dits.controllers;

import com.example.dits.entity.*;
import com.example.dits.service.*;
import com.example.dits.service.impl.StatisticServiceImpl;
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
    private final UserService userService;
    private final QuestionService questionService;
    private final StatisticServiceImpl statisticService;
    private final AnswerService answerService;
    private static List<Question> questionList;
    private static int countOfRightAnswers;
    private static int max;
    private static int counter;
    private static List<Statistic> statistics ;

    @GetMapping("/goTest")
    public String goTest(@RequestParam(value = "testName") String test, @RequestParam(value = "themes") String topic, ModelMap model, HttpSession session){
        questionList = questionService.getQuestionsByTestName(test);
        countOfRightAnswers = 0;
        max = questionList.size();
        counter = 0;
        statistics = new ArrayList<>();
        List<Answer> answers = answerService.getAnswersByQuestion(questionList.get(counter));

        session.setAttribute("testName", test);
        session.setAttribute("topicName", topic);
        session.setAttribute("quantityOfQuestions",max);

        model.addAttribute("question", questionList.get(counter).getDescription());
        model.addAttribute("answers", answers);
        model.addAttribute("counter",++counter);

        return "user/testPage";
    }

    @GetMapping("/nextTestPage")
    public String nextTestPage(@RequestParam(value = "answeredQuestion", required = false) List<Integer> answeredQuestion,
                               ModelMap model,
                               HttpSession session){
        List<Answer> answers = getPrevAnswer();
        saveStatistic(answeredQuestion, session, answers);

        model.addAttribute("question", questionList.get(counter).getDescription());
        model.addAttribute("answers", answers);
        model.addAttribute("counter",++counter);

        return "user/testPage";
    }

    @GetMapping("/resultPage")
    public String testStatistic(@RequestParam(value = "answeredQuestion", required = false) List<Integer> answeredQuestion,
                                ModelMap model,
                                HttpSession session){
        List<Answer> answers = getPrevAnswer();
        saveStatistic(answeredQuestion, session, answers);
        saveStatisticToDB();
        int percents = (int)((double)countOfRightAnswers / max * 100);
        model.addAttribute("rightAnswers",countOfRightAnswers);
        model.addAttribute("countOfQuestions",max);
        model.addAttribute("percentageComplete", ((double)countOfRightAnswers / max));
        model.addAttribute("percents", percents);
        return "user/resultPage";
    }

    private void saveStatisticToDB() {
        Date date = new Date();
        for (var statistic : statistics){
            statistic.setDate(date);
            statisticService.save(statistic);
        }
    }

    private void saveStatistic(List<Integer> answeredQuestion, HttpSession session, List<Answer> answers) {
        var numbersOfRightAnswers = getNumbersOfRightAnswers(answers);
        User currentUser = (User) session.getAttribute("user");

        boolean correctAnswer = false;
        if (numbersOfRightAnswers.equals(answeredQuestion)) {
            correctAnswer = true;
            countOfRightAnswers++;
        }
        statistics.add(new Statistic(new Date(), correctAnswer, questionList.get(counter - 1), currentUser));
    }

    private List<Answer> getPrevAnswer() {
        return answerService.getAnswersByQuestion(questionList.get(counter - 1));
    }

    private List<Integer> getNumbersOfRightAnswers(List<Answer> answers){
        var numberOfRightAnswers = new ArrayList<Integer>();
        for (int i = 0; i < answers.size() ; i++) {
            if (answers.get(i).isCorrect())
            numberOfRightAnswers.add(i);
        }
        return numberOfRightAnswers;
    }
}
