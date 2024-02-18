package com.example.foodplanner.db.PlanDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foodplanner.Model.Meal;
import com.example.foodplanner.Model.Plan;

@Database(entities = {Plan.class}, version = 14)
public abstract class PlanAppDataBase extends RoomDatabase {
    private static PlanAppDataBase instance = null;
    public abstract InterPlanDAO getMealDAO();
    public static synchronized PlanAppDataBase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            PlanAppDataBase.class, "PlanV14")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}


