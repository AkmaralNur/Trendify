package com.example.shoppingmallsystem.util;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingmallsystem.R;
import com.example.shoppingmallsystem.adapter.ShoppongCarGoodsAdapter;
import com.jungly.gridpasswordview.GridPasswordView;

public class PayDialog {

    private static CustomDialog dialog; // Кастомный диалог
    private View view; // Макет диалога
    private GridPasswordView gridPasswordView; // Виджет для ввода пароля
    private RelativeLayout re_bottow; // Нижняя панель
    private RecyclerView recyclerView_carGoods; // Список товаров в корзине
    private TextView tv_clean; // Кнопка очистки корзины
    private TextView tv_DoShopping; // Кнопка для завершения покупки
    private ImageView iv_shoppingCar; // Иконка корзины
    private Context context;
    private ShoppongCarGoodsAdapter shoppongCarGoodsAdapter; // Адаптер для товаров в корзине
    public static TextView tv_total; // Текстовое поле для отображения общей суммы
    public static double total = 0; // Общая сумма товаров в корзине

    // Конструктор, принимает контекст как параметр
    public PayDialog(Context context) {
        this.context = context;
        dialog = new CustomDialog(context, R.style.PayDialog); // Создание диалога с пользовательским стилем
        view = LayoutInflater.from(context).inflate(R.layout.pay_dialog, null); // Подключение макета

        // Получение ссылки на виджет для ввода пароля
        gridPasswordView = view.findViewById(R.id.my_passwordView);

        // Установка слушателя для изменения пароля
        gridPasswordView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            // Вызывается при изменении текста в поле пароля
            public void onTextChanged(String psw) {
                // Пример: tv.setText("Пароль вводится...");
            }
            // Вызывается при завершении ввода пароля
            public void onInputFinish(String psw) {
                Log.e("Введённый пароль", psw);
            }
        });

        // Настройка действий перед закрытием диалога
        dialog.setBeforeDismiss(new CustomDialog.IBeforeDismiss() {
            @Override
            public void onBeforeDismiss() {
                dismissAnim(); // Анимация при закрытии
            }
        });

        // Получение окна диалога
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent); // Установка прозрачного фона

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // Установка ширины на весь экран
        lp.height = WindowManager.LayoutParams.MATCH_PARENT; // Установка высоты на весь экран
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM); // Установка диалога внизу экрана
    }

    // Метод для отображения диалога
    public void show() {
        dialog.show();
        showAnim(); // Запуск анимации показа
    }

    // Анимация показа диалога
    private void showAnim() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, DensityUtil.dp2px(context, 300), 0);
        animation.setDuration(300); // Продолжительность анимации 300 мс
    }

    // Анимация закрытия диалога
    private void dismissAnim() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, DensityUtil.dp2px(context, 300));
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Действия в начале анимации
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dialog.myDismiss(); // Закрытие диалога после окончания анимации
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Действия при повторении анимации (если нужно)
            }
        });
        animation.setDuration(300); // Продолжительность анимации 300 мс
    }
}
