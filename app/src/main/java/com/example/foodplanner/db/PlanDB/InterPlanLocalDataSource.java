package com.example.foodplanner.db.PlanDB;

import com.example.foodplanner.Model.Plan;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

public interface InterPlanLocalDataSource {
    Observable<List<String>> getDayMealsData(String columnName);

    Flowable<List<Plan>> getAllPlans();


    void insertDayMealData(Plan plan);

    void deleteAllPlanData();

    void deleteSatMealData(String mealDetails);
    void deleteSunMealData(String mealDetails);
    void deleteMonMealData(String mealDetails);
    void deleteTueMealData(String mealDetails);
    void deleteWedMealData(String mealDetails);
    void deleteThMealData(String mealDetails);
    void deleteFriMealData(String mealDetails);


//    void deleteFromDayMeal(String columnName,String mealDetails);


}
