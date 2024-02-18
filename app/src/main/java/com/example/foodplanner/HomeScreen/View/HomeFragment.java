package com.example.foodplanner.HomeScreen.View;

import static com.example.foodplanner.Online.LoginFragment.SHARED_PREF;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Favourate.Presenter.FavMealsPresenter;
import com.example.foodplanner.Favourate.Presenter.InterFavMealsPresenter;
import com.example.foodplanner.Model.MealRepositoryInter;
import com.example.foodplanner.Online.StartActivity;
import com.example.foodplanner.Plans.View.DetailsOfMealActivity;
import com.example.foodplanner.HomeScreen.Presenter.HomeScreenPresenter;
import com.example.foodplanner.HomeScreen.Presenter.HomeScreenPresenterInter;
import com.example.foodplanner.Model.Meal;
import com.example.foodplanner.Model.MealRepository;
import com.example.foodplanner.R;
import com.example.foodplanner.db.FavDB.FavLocalDataSource;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class HomeFragment extends Fragment
            implements HomeFragmentInter,OnAddMealListener{

    //click on meal
    public static final String EXTRA_MEAL = "mealTag";
    private static final String TAG = "HomeFragment";
    RecyclerView chickenRecyclerView;
    RecyclerView beefRecyclerView;
    RecyclerView seaFoodRecyclerView;

    TextView randomMealName;

    Button btnRandom;

    FloatingActionButton btnLogout;
    FirebaseAuth mAuth;


    View viewFrag;
    ImageView randomMealImg;

    HomeScreenPresenterInter homeScreenPresenterInter;

    HomeActivity homeActivity;

    DatabaseReference reference;
    FirebaseDatabase fireDB;

    MealRepositoryInter mealRepositoryInter;

    InterFavMealsPresenter interFavMealsPresenter;

    List<Meal> emptyList=new ArrayList<Meal>();

    Meal emptyMeal=new Meal("","","","","","");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        randomMealName=view.findViewById(R.id.random_name);
        randomMealImg=view.findViewById(R.id.random_img);
        btnRandom=view.findViewById(R.id.btn_random);
        btnLogout=view.findViewById(R.id.btn_logout);
        viewFrag=view;
        homeActivity=(HomeActivity)getActivity();
        emptyList.add(emptyMeal);

        SharedPreferences sharedPreferences =
                homeActivity.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("name","");
        Log.i(TAG, "userName: "+userName);

        mAuth=FirebaseAuth.getInstance();

        chickenRecyclerView=view.findViewById(R.id.Chicken_View);
        beefRecyclerView=view.findViewById(R.id.Beef_view);
        seaFoodRecyclerView=view.findViewById(R.id.SeaFood_view);

        homeScreenPresenterInter = new HomeScreenPresenter(
                this,
                MealRepository.getFavInstance(
                        MealsRemoteDataSource.getInstance()
                        , FavLocalDataSource.getInstance(viewFrag.getContext())));

        homeScreenPresenterInter.getRandomMealPres();
        homeScreenPresenterInter.getMealsOfCategoryPres("Chicken");
        homeScreenPresenterInter.getMealsOfCategoryPres("Beef");
        homeScreenPresenterInter.getMealsOfCategoryPres("Seafood");

        mealRepositoryInter = MealRepository.getFavInstance(
                MealsRemoteDataSource.getInstance(),
                FavLocalDataSource.getInstance(viewFrag.getContext()));
        interFavMealsPresenter = new FavMealsPresenter(mealRepositoryInter);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {
                fireDB=FirebaseDatabase.getInstance();
                reference=fireDB.getReference("users");
                interFavMealsPresenter.getStoredDataDB()
                        .observeOn(AndroidSchedulers.mainThread()).map(mealInfoList -> {
                            Log.i(TAG, "resume: ");
                            List<Meal> myMeals = new ArrayList<>();
                            for (Meal meal : mealInfoList) {
                                myMeals.add(meal);
                            }
                            return myMeals;
                        })
                        .subscribe(
                                mealsInfo -> {
                                    Log.i(TAG, "Obs: "+ mealsInfo.get(0).getName());
                                    if(!mealsInfo.get(0).getName().equals("")){
                                        reference.child(userName).setValue(mealsInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
//                                            mealsInfo.clear();
//                                            for(Meal myMeal : mealsInfo){
//                                                interFavMealsPresenter.removeFavMeal(myMeal);
//                                                Log.i(TAG, "clear: "+myMeal.getName());
//                                            }
                                                Log.i(TAG, "size of favList: "+mealsInfo.size());
                                                Toast.makeText(homeActivity, "Added !", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else{

                                        reference.child(userName).setValue(emptyList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
//                                            mealsInfo.clear();
//                                            for(Meal myMeal : mealsInfo){
//                                                interFavMealsPresenter.removeFavMeal(myMeal);
//                                                Log.i(TAG, "clear: "+myMeal.getName());
//                                            }
                                                Log.i(TAG, "size of favList: "+mealsInfo.size());
                                                Toast.makeText(homeActivity, "Added !", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }


                                },
                                err -> Log.i(TAG, "ObsError: failure "),
                                () -> Log.i(TAG, "ObsComp: ")
                        );



                List<Meal> meals= new ArrayList<Meal>();

                meals.add(new Meal("1","mina","","","",""));
                meals.add(new Meal("2","emad","","","",""));




                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", "");
                editor.apply();
                mAuth.signOut();
                Intent intent = new Intent(homeActivity, StartActivity.class);
                startActivity(intent);
                homeActivity.finish();
                Toast.makeText(homeActivity, "Logout Successful !", Toast.LENGTH_SHORT).show();
            }
        });


        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mealName = randomMealName.getText().toString();
                Intent intent = new Intent(viewFrag.getContext(),
                        DetailsOfMealActivity.class);
                intent.putExtra(EXTRA_MEAL,mealName);
                startActivity(intent);
            }
        });
    }

    @Override
    public void showChickenCategory(List<Meal> meals) {
        setRecyclerViewAdpter(chickenRecyclerView,meals);
    }

    @Override
    public void showBeefCategory(List<Meal> meals) {
        setRecyclerViewAdpter(beefRecyclerView,meals);
    }

    @Override
    public void showSeaFoodCategory(List<Meal> meals) {
        setRecyclerViewAdpter(seaFoodRecyclerView,meals);
    }



    @Override
    public void showRandomMeal(List<Meal> meals) {
        randomMealName.setText(meals.get(0).getName());
            Glide.with(viewFrag.getContext()).load(meals.get(0).getThumbnail())
                    .into(randomMealImg);
    }

    @Override
    public void showErr(String err) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(homeActivity);
//        builder.setMessage(err).setTitle("An Error Occurred");
//        AlertDialog dialog = builder.create();
//        dialog.show();
    }

    @Override
    public void onMealClick(Meal meal) {
        homeScreenPresenterInter.addFavMeal(meal);
    }


    public void setRecyclerViewAdpter(RecyclerView recyclerView, List<Meal> meals){
        LinearLayoutManager linearManager = new LinearLayoutManager(viewFrag.getContext());
        linearManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        HomeCategoryAdapter homeCategoryAdapter =
                new HomeCategoryAdapter(viewFrag.getContext(), new ArrayList<>(),
                        this,
                        this);
        recyclerView.setLayoutManager(linearManager);
        recyclerView.setAdapter(homeCategoryAdapter);

        //click on meal
        homeCategoryAdapter.setClickMeal((view1, position) -> {
            String mealName = homeCategoryAdapter.getItem(position).getName();
            Intent intent = new Intent(viewFrag.getContext(),
                    DetailsOfMealActivity.class);
            intent.putExtra(EXTRA_MEAL,mealName);
            startActivity(intent);
        });

        homeCategoryAdapter.setMyList(meals);
        homeCategoryAdapter.notifyDataSetChanged();
    }
}