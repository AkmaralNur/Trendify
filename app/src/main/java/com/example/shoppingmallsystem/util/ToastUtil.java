package com.example.shoppingmallsystem.util;

import android.widget.Toast;

public class ToastUtil {
    private ToastUtil() {

        throw new UnsupportedOperationException("Экземпляр этого класса не может быть создан");
    }

    public static void showShort(String message) {
        Toast.makeText(AppContext.getInstance(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String message) {
        Toast.makeText(AppContext.getInstance(), message, Toast.LENGTH_LONG).show();
    }
}
