package com.example.foodplanner.HomeScreen.Presenter;

import com.example.foodplanner.HomeScreen.View.HomeFragmentInter;
import com.example.foodplanner.Model.Meal;
import com.example.foodplanner.Model.MealRepositoryInter;
import com.example.foodplanner.network.CallBackInter;

import java.util.List;

public class HomeScreenPresenter implements
        HomeScreenPresenterInter, CallBackInter {

    private HomeFragmentInter homeFragmentInter;

    private MealRepositoryInter mealRepositoryInter;


    public HomeScreenPresenter(HomeFragmentInter interAllProductsView,
                                MealRepositoryInter interProductsRepository) {
        this.homeFragmentInter = interAllProductsView;
        this.mealRepositoryInter = interProductsRepository;
    }


//    @Override
//    public void getChickenMeals(String query) {
//
//        mealRepositoryInter.getAllChickenMeals(this,query);
//    }
//
//    @Override
//    public void getCategoryMeals(String query) {
//        mealRepositoryInter.getAllBeefMeals(this,query);
//
//    }

    @Override
    public void getCategoryMeals() {
        mealRepositoryInter.getAllMeals(this);
    }

    @Override
    public void getRandomMeal() {
        mealRepositoryInter.getRandomMeal(this);
    }

    @Override
    public void onSuccessChicken(List<Meal> meals) {
            homeFragmentInter.showChickenCategory(meals);
    }

    @Override
    public void onSuccessBeef(List<Meal> meals) {
        homeFragmentInter.showBeefCategory(meals);
    }

    @Override
    public void onSuccessRandom(List<Meal> meals) {
        homeFragmentInter.showRandomMeal(meals);
    }

    @Override
    public void onFail(String err) {
        //homeFragmentInter.showErr(err);
    }
}
