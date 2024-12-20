package com.example.shoppingmallsystem.util;

import android.app.Application;

public class AppContext extends Application {
    // Создание статической приватной переменной для хранения экземпляра контекста
    private static AppContext instance;

    // Создание статического метода для возвращения необходимого экземпляра контекста
    public static AppContext getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Присваиваем контекст приложения переменной instance
        this.instance = AppContext.this;
    }

}
