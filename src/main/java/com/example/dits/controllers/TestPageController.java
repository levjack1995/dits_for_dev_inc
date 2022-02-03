package com.example.dits.controllers;

import com.example.dits.entity.*;
import com.example.dits.service.*;
import com.example.dits.service.impl.StatisticServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.util.Precision;
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

    @GetMapping("/goTest")
    public String goTest(@RequestParam int testId, @RequestParam(value = "topicName") String topicName, ModelMap model, HttpSession session){
        Test test = testService.getTestByTestId(testId);
        List<Question> questionList = questionService.getQuestionsByTest(test);
        int quantityOfQuestions = questionList.size();
        int questionNumber = 0;
        int quantityOfRightAnswers = 0;

        List<Answer> answers = getAnswersForQuestionFromQuestionList(questionList, questionNumber);
        String questionDescription = getDescriptionForQuestionFromQuestionList(questionList, questionNumber);

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

    private String getDescriptionForQuestionFromQuestionList(List<Question> questionList, int index) {
        return questionList.get(index).getDescription();
    }

    private List<Answer> getAnswersForQuestionFromQuestionList(List<Question> questionList, int index) {
        return answerService.getAnswersByQuestion(questionList.get(index));
    }

    @GetMapping("/nextTestPage")
    public String nextTestPage(@RequestParam(value = "answeredQuestion", required = false) List<Integer> answeredQuestion,
                               ModelMap model,
                               HttpSession session){

        List<Question> questionList = (List<Question>) session.getAttribute("questions");
        int questionNumber = (int) session.getAttribute("questionNumber");
        User user = (User) session.getAttribute("user");

        List<Answer> answers = getAnswersForQuestionFromQuestionList(questionList, questionNumber);
        String questionDescription = getDescriptionForQuestionFromQuestionList(questionList, questionNumber);

        boolean isCorrect = isRightAnswer(answeredQuestion,questionList,questionNumber);
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

    private boolean isRightAnswer(List<Integer> answeredQuestion, List<Question> questionList, int questionNumber) {
        List<Answer> prevAnswer = getPreviousAnswers(questionList, questionNumber);
        List<Integer> rightIndexesList = getListOfIndexesOfRightAnswers(prevAnswer);
        // проверить на сравнение.
        return answeredQuestion.equals(rightIndexesList);
    }

    private List<Integer> getListOfIndexesOfRightAnswers(List<Answer> prevAnswer) {
        List<Integer> rightAnswers = new ArrayList<>();
        for (int i = 0; i < prevAnswer.size(); i++) {
            if (prevAnswer.get(i).isCorrect()){
                rightAnswers.add(i);
            }
        }
        return rightAnswers;
    }

    private List<Answer> getPreviousAnswers(List<Question> questionList, int questionNumber) {
        return answerService.getAnswersByQuestion(questionList.get(questionNumber - 1));
    }

    @GetMapping("/resultPage")
    public String testStatistic(@RequestParam(value = "answeredQuestion", required = false) List<Integer> answeredQuestion,
                                ModelMap model,
                                HttpSession session){

        List<Question> questions = (List<Question>) session.getAttribute("questions");
        int questionNumber = questions.size() - 1;
        boolean isCorrect = isRightAnswer(answeredQuestion,questions,questionNumber);
        User user = (User) session.getAttribute("user");

        List<Statistic> statisticList = (List<Statistic>) session.getAttribute("statistics");
        statisticList.add(Statistic.builder()
                .question(questions.get(questionNumber))
                .user(user)
                .correct(isCorrect).build());

        int countOfRightAnswers = 0;
        Date date = new Date();

        for (Statistic statistic : statisticList){
            statistic.setDate(date);
            if (statistic.isCorrect())
                countOfRightAnswers++;
        }

        saveStatisticToDB(statisticList);

        double percentOfRightAnswers = (double)countOfRightAnswers / questions.size();

        percentOfRightAnswers = Precision.round((percentOfRightAnswers), 0);
        model.addAttribute("rightAnswers",countOfRightAnswers);
        model.addAttribute("countOfQuestions", questions.size());
        model.addAttribute("percentageComplete", (percentOfRightAnswers));
//        model.addAttribute("percents", percents);
        return "user/resultPage";
    }

    private void saveStatisticToDB(List<Statistic> statistics) {
      statistics.stream().forEach(statisticService::save);
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
