package com.ray_world.rweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 15-5-3.
 */
public class RWeatherOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_SELECTED_CITY = "create table SelectedCity ("
            + "id integer primary key autoincrement, "
            + "selected_district_name txt, "
            + "selected_city_name text, "
            + "selected_city_temp text)";

    public RWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SELECTED_CITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
