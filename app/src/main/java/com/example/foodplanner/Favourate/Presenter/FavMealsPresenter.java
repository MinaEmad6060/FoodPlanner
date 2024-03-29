package com.example.foodplanner.Favourate.Presenter;


import com.example.foodplanner.Model.Meal;
import com.example.foodplanner.Model.MealRepositoryInter;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class FavMealsPresenter implements InterFavMealsPresenter {


    private MealRepositoryInter mealRepositoryInter;


    public FavMealsPresenter(MealRepositoryInter mealRepositoryInter) {
        this.mealRepositoryInter = mealRepositoryInter;
    }

    @Override
    public Flowable<List<Meal>> getStoredDataDB() {
        return mealRepositoryInter.getStoredMeals();
    }

    @Override
    public void removeFavMeal(Meal meal) {
        mealRepositoryInter.deleteMeals(meal);
    }
}
