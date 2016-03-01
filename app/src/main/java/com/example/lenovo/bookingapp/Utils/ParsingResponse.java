package com.example.lenovo.bookingapp.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

/**
 * Created by deii on 11/3/2015.
 */
public class ParsingResponse {

    public <T> ArrayList<T> parseJsonArrayWithJsonObject(JSONArray jsonArray, Class modelClass) {
        Object obj = null;

        ArrayList<T> data = new ArrayList<>();

        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                obj = modelClass.newInstance();
                if (obj instanceof String || obj instanceof Integer || obj instanceof Boolean) {
                    Object undefinedObj = jsonArray.opt(i);
                    if (undefinedObj != null && !undefinedObj.equals(null) && !undefinedObj.equals(""))
                        data.add((T) undefinedObj);
                } else {
                    for (Field f : modelClass.getDeclaredFields()) {
                        f.setAccessible(true);

                        Object undefinedObj = jsonArray.opt(i);


                        if (undefinedObj instanceof JSONObject) {
                            String name = f.getName();
                            Object undefinedInnerObj = ((JSONObject) undefinedObj).opt(name);
                            if (undefinedInnerObj instanceof JSONArray) {
                                ParameterizedType stringListType = (ParameterizedType) f.getGenericType();
                                Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
                                ArrayList<T> list = parseJsonArrayWithJsonObject((JSONArray) undefinedInnerObj, stringListClass);
                                f.set(obj, list);
                            } else if (undefinedInnerObj != null)
                                f.set(obj, undefinedInnerObj);

                        } else {
                            f.set(obj, undefinedObj);
                        }
                    }
                    data.add((T) obj);
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return data;
    }

    public <T> T parseJsonObject(JSONObject jsonObject, Class modelClass) {

        Object object = null;

        T data = null;

        try {
            object = modelClass.newInstance();

            for (Field f : modelClass.getDeclaredFields()) {

                f.setAccessible(true);
                Object undefinedInnerObj = jsonObject.opt(f.getName());
                if (undefinedInnerObj instanceof JSONArray) {

                    ParameterizedType stringListType = (ParameterizedType) f.getGenericType();
                    Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
                    ArrayList<T> list = parseJsonArrayWithJsonObject((JSONArray) undefinedInnerObj, stringListClass);
                    f.set(object, list);

                } else
                    f.set(object, jsonObject.opt(f.getName()));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = (T) object;

        return data;
    }
}
