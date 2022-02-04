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

        //Логика получения данных
        Test test = testService.getTestByTestId(testId);
        List<Question> questionList = questionService.getQuestionsByTest(test);
        int quantityOfQuestions = questionList.size();
        int questionNumber = 0;
        int quantityOfRightAnswers = 0;
        //Левая логика

        List<Answer> answers = answerService.getAnswersFromQuestionList(questionList, questionNumber);
        String questionDescription = questionService.getDescriptionFromQuestionList(questionList, questionNumber);

        //Логика запихивания данных
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
        //Получение данных с сессии и модели
        List<Question> questionList = (List<Question>) session.getAttribute("questions");
        int questionNumber = (int) session.getAttribute("questionNumber");
        User user = (User) session.getAttribute("user");
        //Логика на проверку правильности вопроса
        boolean isCorrect = answerService.isRightAnswer(answeredQuestion,questionList,questionNumber);

        //Посмотреть мб можно как-то их в убрать это дублирование кода?
        List<Answer> answers = answerService.getAnswersFromQuestionList(questionList, questionNumber);
        String questionDescription = questionService.getDescriptionFromQuestionList(questionList, questionNumber);

        //Получение данных с сессии
        List<Statistic> statisticList = (List<Statistic>) session.getAttribute("statistics");
        //Добавление данных в статистику - логика контроллера
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

        //Получение данных
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

        //Получение данных и запихивание в модель


        int countOfRightAnswers = statisticService.calculateRightAnswers(statisticList);
        statisticService.saveStatisticsToDB(statisticList);

        int percentOfRightAnswers = (int) answerService.countPercentsOfRightAnswers(countOfRightAnswers,questions.size());

        model.addAttribute("rightAnswers",countOfRightAnswers);
        model.addAttribute("countOfQuestions", questions.size());
        model.addAttribute("percentageComplete", percentOfRightAnswers);
//        model.addAttribute("percents", percents);
        return "user/resultPage";
    }

    private boolean isResultPage(int questionNumber, List<Statistic> statisticList) {
        return statisticList.size() >= questionNumber;
    }

//    private void saveStatisticToDB(List<Statistic> statistics) {
//      statistics.stream().forEach(statisticService::save);
//    }

//    private List<Integer> getNumbersOfRightAnswers(List<Answer> answers){
//        var numberOfRightAnswers = new ArrayList<Integer>();
//        for (int i = 0; i < answers.size() ; i++) {
//            if (answers.get(i).isCorrect())
//            numberOfRightAnswers.add(i);
//        }
//        return numberOfRightAnswers;
//    }
}
