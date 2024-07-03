package com.example.youtube.Converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.youtube.entities.comment;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class videoConverters {
    @TypeConverter
    public static ArrayList<comment> fromString(String value) {
        Type listType = new TypeToken<ArrayList<comment>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<comment> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
