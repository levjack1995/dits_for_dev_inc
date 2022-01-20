package com.example.dits.facades;

import com.example.dits.dto.TestStatistic;
import com.example.dits.dto.TestStatisticByDate;
import com.example.dits.dto.UserStatistics;
import com.example.dits.entity.Statistic;
import com.example.dits.entity.User;
import com.example.dits.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class StatisticFacade {

    private final StatisticService statisticService;
    private static final  int initValue = 0;

    @Transactional
    public UserStatistics getUserStatistics(User user){
        List<Statistic> statistics = statisticService.getStatisticsByUser(user);
        Map<Date, ArrayList<Statistic>> statisticByDate  = getStatisticsByDate(statistics);

        List<TestStatisticByDate> testStatisticsByDate = getTestStatisticByDate(statisticByDate);
        Map<String, TestStatistic> staticticByName = getMapTestStatisticsByTestName(testStatisticsByDate);
        List<TestStatistic> testStatisticList = new ArrayList<>(staticticByName.values());

        UserStatistics userStatistics = new UserStatistics(user.getFirstName(),user.getLastName(),user.getLogin(),testStatisticList);
        return userStatistics;
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
