package com.example.shoppingmallsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.shoppingmallsystem.MainActivity;
import com.example.shoppingmallsystem.R;
import com.example.shoppingmallsystem.bean.Userinfo;
import com.example.shoppingmallsystem.util.MySQLiteHelper;
import java.text.DecimalFormat;

/**
 *  Активность "Мой кошелёк"
 */
public class MyAccountActivity extends AppCompatActivity {

    private TextView tv_bar_title; // Название панели инструментов
    private Toolbar toolbar; // Панель инструментов
    private TextView tv_recharge; // Кнопка "Пополнить"
    private TextView tv_userName; // Имя пользователя
    private TextView tv_money; // Сумма денег
    private TextView tv_phoneNumb; // Номер телефона
    private TextView tv_schoolName; // Название школы
    private TextView tv_apartmentNumb; // Номер общежития
    private ImageView iv_personal_pic; // Изображение профиля
    private Userinfo userinfo; // Информация о пользователе
    private double newMoney; // Новая сумма денег
    private int myUserID; // ID пользователя
    private DecimalFormat df; // Формат для отображения чисел
    private String result; // Результат форматирования

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        initData(); // Инициализация данных
        initView(); // Инициализация представлений
        setActionBar(); // Установка панели инструментов
    }

    /** Установка панели инструментов */
    private void setActionBar() {
        setSupportActionBar(toolbar);
        /* Отображение иконки "Домой" */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Отключение отображения названия проекта
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    // Обработка нажатия на кнопку "Назад" в панели инструментов
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        // Получение ID пользователя и информации о нём из базы данных
        userinfo = MySQLiteHelper.getInstance(getApplicationContext()).getUserInfoFromUserName(MainActivity.username);
        myUserID = MySQLiteHelper.getInstance(getApplicationContext()).GetUserId(MainActivity.username);
    }

    private void initView() {
        // Инициализация элементов интерфейса
        toolbar = findViewById(R.id.toolbar);
        tv_bar_title = findViewById(R.id.tv_bar_title);
        tv_bar_title.setText("Мой кошелёк");
        tv_recharge = findViewById(R.id.tv_bar_function);
        tv_recharge.setVisibility(View.VISIBLE);
        tv_userName = findViewById(R.id.tv_username_acc);
        tv_money = findViewById(R.id.tv_money);
        tv_phoneNumb = findViewById(R.id.tv_phoneNumb_acc);
        tv_schoolName = findViewById(R.id.tv_schoolName_acc);
        tv_apartmentNumb = findViewById(R.id.tv_apartmentNumb_acc);
        iv_personal_pic = findViewById(R.id.iv_personal_pic);

        tv_userName.setText(userinfo.getUserName());
        // Отключение научной нотации при отображении чисел
        df = new DecimalFormat("0.00");
        result = df.format(userinfo.getMoney());
        tv_money.setText(result + " сом");
        tv_phoneNumb.setText(userinfo.getPhoneNumb());
        tv_schoolName.setText(userinfo.getSchoolName());
        tv_apartmentNumb.setText(userinfo.getApartmentNumb());

        // Установка изображения профиля в зависимости от ID пользователя
        switch (myUserID) {
            case 1:
                iv_personal_pic.setImageResource(R.drawable.tx_1_48);
                break;
            case 2:
                iv_personal_pic.setImageResource(R.drawable.tx_2_48);
                break;
            case 3:
                iv_personal_pic.setImageResource(R.drawable.tx_3_48);
                break;
            case 4:
                iv_personal_pic.setImageResource(R.drawable.tx_4_48);
                break;
            case 5:
                iv_personal_pic.setImageResource(R.drawable.tx_5_48);
                break;
            case 6:
                iv_personal_pic.setImageResource(R.drawable.tx_6_48);
                break;
        }

        /** Обработка нажатия на кнопку "Пополнить" */
        tv_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MyAccountActivity.this, RechargeActivity.class), 0);
            }
        });
    }

    /**
     * Обновление данных о сумме денег после пополнения
     */
    private void refreshMoney() {
        newMoney = MySQLiteHelper.getInstance(getApplicationContext()).getUserMoneyFromUserName(MainActivity.username);
        // Отключение научной нотации
        df = new DecimalFormat("0.00");
        result = df.format(newMoney);
        tv_money.setText(result + " сом");
    }

    /**
     * Обработка результата возвращения с другой активности
     * @param requestCode Код запроса
     * @param resultCode Код результата
     * @param data Данные
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            refreshMoney();
        } else {
            return;
        }
    }
}
