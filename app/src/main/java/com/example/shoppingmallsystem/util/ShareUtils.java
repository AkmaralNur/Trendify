package com.example.shoppingmallsystem.util;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareUtils {
    // Получаем экземпляр SharedPreferences
    static SharedPreferences preferences = AppContext.getInstance().getSharedPreferences("data", Context.MODE_PRIVATE);
    // Редактор для изменения данных SharedPreferences
    static SharedPreferences.Editor editor = preferences.edit();

    public static String getAuto_Login() {
        return preferences.getString("AUTO_LOGIN", ""); // Возвращает пустую строку, если значение отсутствует
    }

    public static void putAuto_Login(String tag) {
        editor.putString("AUTO_LOGIN", tag);
        editor.commit(); // Применение изменений
    }
    public static String getRember() {
        return preferences.getString("Rember", ""); // Возвращает пустую строку, если значение отсутствует
    }

    public static void putRember(String isRember) {
        editor.putString("Rember", isRember);
        editor.commit(); // Применение изменений
    }

    public static void putUserName(String username) {
        editor.putString("UserName", username);
        editor.commit(); // Применение изменений
    }

    public static String getUserName() {
        return preferences.getString("UserName", ""); // Возвращает пустую строку, если значение отсутствует
    }

    public static void putPassword(String password) {
        editor.putString("Password", password);
        editor.commit(); // Применение изменений
    }

    public static String getPassword() {
        return preferences.getString("Password", ""); // Возвращает пустую строку, если значение отсутствует
    }
}
