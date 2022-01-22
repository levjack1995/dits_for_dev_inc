package com.example.dits.facades;

import com.example.dits.dto.*;
import com.example.dits.entity.*;
import com.example.dits.service.StatisticService;
import com.example.dits.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class StatisticFacade {

    private final StatisticService statisticService;
    private final TopicService topicService;
    private static final  int initValue = 0;

    @Transactional
    public UserStatistics getUserStatistics(User user){
        List<TestStatistic> testStatisticList = getTestStatisticsByUser(user);

        UserStatistics userStatistics = new UserStatistics(user.getFirstName(),user.getLastName(),user.getLogin(),testStatisticList);
        return userStatistics;
    }

    private List<TestStatistic> getTestStatisticsByUser(User user) {
        List<Statistic> statistics = statisticService.getStatisticsByUser(user);
        Map<Date, ArrayList<Statistic>> statisticByDate  = getStatisticsByDate(statistics);

        List<TestStatisticByDate> testStatisticsByDate = getTestStatisticByDate(statisticByDate);
        Map<String, TestStatistic> statisticByTestName = getMapTestStatisticsByTestName(testStatisticsByDate);
        List<TestStatistic> testStatisticList = new ArrayList<>(statisticByTestName.values());
        return testStatisticList;
    }

    public List<TopicStatisticByTests> getTopicStaticByTests(){

        List<Topic> topics = topicService.findAll();
        List<TopicStatisticByTests> topicStatisticByTests = new ArrayList<>();

        for (Topic topic : topics) {
            List<Test> testLists= topic.getTestList();
            List<TestStatistic> testStatistics = new ArrayList<>();

            for (Test test : testLists){

                List<Question> questionList = test.getQuestions();
                List<QuestionStatistic> questionStatistics = new ArrayList<>();
                Set<Date> uniqueDate = new HashSet<>();
                int count = 0;
                int questionAvg = 0;
                int testSumAvg = 0;
                for (Question question : questionList){

                    List<Statistic> statisticList = statisticService.getStatisticByQuestion(question);
                    int rightAnswer = 0;
                    for (Statistic statistic : statisticList){
                        uniqueDate.add(statistic.getDate());
                        if (statistic.isCorrect())
                            rightAnswer++;
                    }

                    count = uniqueDate.size();
                    if (count != 0)
                    questionAvg = calculateAvg(count, rightAnswer);

                    testSumAvg += questionAvg;
                    questionStatistics.add(new QuestionStatistic(question.getDescription(),count,questionAvg));
                }

                int questionStatisticsSize = questionStatistics.size();
                int testAverage = calculateAvg(questionStatisticsSize, testSumAvg);
                testStatistics.add(new TestStatistic(test.getName(),count,testAverage, questionStatistics));
            }
            topicStatisticByTests.add(new TopicStatisticByTests(topic.getName(),
                    topic.getDescription(),testStatistics));
        }

        return topicStatisticByTests;
    }

    private int calculateAvg(int count, double rightAnswer) {
        return (int) (rightAnswer / count);
    }

    private Map<String, TestStatistic> getMapTestStatisticsByTestName(List<TestStatisticByDate> testStatisticsByDate) {
        Map<String, TestStatistic> staticticByName = new HashMap<>();
        for (var st : testStatisticsByDate){
            if (!staticticByName.containsKey(st.getTestName())) {
                TestStatistic testStatistic = new TestStatistic(st.getTestName(),initValue,st.getAvg());
                staticticByName.put(st.getTestName(), testStatistic);
            }
            else{
                TestStatistic testStatistic = staticticByName.get(st.getTestName());
                double sumOfAvg = testStatistic.getAvgProc() * testStatistic.getCount();
                testStatistic.setCount(testStatistic.getCount() + 1);
                int finishAvg =(int)((sumOfAvg + st.getAvg()) / testStatistic.getCount());
                testStatistic.setAvgProc(finishAvg);
            }
        }
        return staticticByName;
    }


    @Transactional
    List<TestStatisticByDate> getTestStatisticByDate(Map<Date, ArrayList<Statistic>> statisticByDate) {
        List<TestStatisticByDate> testStatisticsByDate = new ArrayList<>();
        for (var values : statisticByDate.values()){
            double countOfRightAnswers = 0;
            TestStatisticByDate testStatisticByDate = new TestStatisticByDate();
            System.out.println(values.get(0).getQuestion().getTest().getName());
            testStatisticByDate.setTestName(values.get(0).getQuestion().getTest().getName());
            for (var st : values){
                if(st.isCorrect()){
                    countOfRightAnswers++;
                }
            }

            int avg = (int) ((countOfRightAnswers / values.size())* 100);
            testStatisticByDate.setAvg(avg);
            testStatisticsByDate.add(testStatisticByDate);
        }
        return testStatisticsByDate;
    }

    private Map<Date, ArrayList<Statistic>> getStatisticsByDate(List<Statistic> statistics) {
        Map<Date, ArrayList<Statistic>> statisticsByDate = new HashMap<>();
        for (var st : statistics){
            if(!statisticsByDate.containsKey(st.getDate())){
                ArrayList<Statistic> statisticList = new ArrayList<>();
                statisticsByDate.put(st.getDate(),statisticList);
                statisticList.add(st);
            }
            else{
                statisticsByDate.get(st.getDate()).add(st);
            }
        }
        return statisticsByDate;
    }
}
