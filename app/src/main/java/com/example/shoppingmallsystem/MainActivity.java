package com.example.shoppingmallsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoppingmallsystem.activity.HomeAllStoresActivity;
import com.example.shoppingmallsystem.activity.RegisterActivity;
import com.example.shoppingmallsystem.bean.Userinfo;
import com.example.shoppingmallsystem.util.MySQLiteHelper;
import com.example.shoppingmallsystem.util.ShareUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_password; // Поле для ввода пароля
    private EditText et_username; // Поле для ввода имени пользователя
    private Button btn_login; // Кнопка "Войти"
    private Button btn_register; // Кнопка "Регистрация"
    public static String username; // Имя пользователя
    private String password; // Пароль
    private CheckBox rember; // Чекбокс "Запомнить пароль"
    private CheckBox autologin; // Чекбокс "Автоматический вход"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Установка макета
        initView(); // Инициализация элементов интерфейса
    }
    private void initView() {
        et_password = (EditText) findViewById(R.id.et_password);
        et_username = (EditText) findViewById(R.id.et_username);
        btn_login = (Button) findViewById(R.id.M_login);
        btn_register = (Button) findViewById(R.id.M_register);
        rember = (CheckBox) findViewById(R.id.remenberpw);
        autologin = (CheckBox) findViewById(R.id.autologin);

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        // Очистка всех данных в таблице (для тестирования)
        // MySQLiteHelper.getInstance(this).deleateAllUserInfo();

        // Вывод всех записей имени пользователя и пароля в лог
        List<Userinfo> userinfoList = MySQLiteHelper.getInstance(this).queryAlluserInfo();
        // Log.e("userinfoList", userinfoList.toString());

        // Проверка состояния чекбокса "Запомнить пароль"
        if (ShareUtils.getRember().equals("1")) {
            rember.setChecked(true);
            et_username.setText(ShareUtils.getUserName());
            et_password.setText(ShareUtils.getPassword());
        } else {
            rember.setChecked(false);
        }

        // Проверка состояния чекбокса "Автоматический вход"
        if (ShareUtils.getAuto_Login().equals("1")) {
            autologin.setChecked(true);
            if (TextUtils.isEmpty(et_username.getText()) || TextUtils.isEmpty(et_password.getText())) {
                Toast.makeText(this, "Имя пользователя или пароль пустые", Toast.LENGTH_SHORT).show();
            } else {
                // Переход на страницу после успешного входа
                startActivity(new Intent(this, HomeAllStoresActivity.class));
                username = ShareUtils.getUserName();
                this.finish();
            }
        } else {
            autologin.setChecked(false);
        }

        // Обработка изменений состояния чекбокса "Запомнить пароль"
        rember.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rember.isChecked()) {
                    ShareUtils.putRember("1"); // Установить флаг "запомнить"
                } else {
                    ShareUtils.putRember("0"); // Сбросить флаг
                }
            }
        });

        // Обработка изменений состояния чекбокса "Автоматический вход"
        autologin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (autologin.isChecked()) {
                    ShareUtils.putAuto_Login("1"); // Установить флаг "автоматический вход"
                } else {
                    ShareUtils.putAuto_Login("0"); // Сбросить флаг
                }
            }
        });
    }

    /**
     * Обработка нажатий на кнопки.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.M_login:
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "Имя пользователя или пароль не могут быть пустыми", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (MySQLiteHelper.getInstance(this).queryUseristrue(username, password)) {
                    if (rember.isChecked()) {
                        ShareUtils.putUserName(username);
                        ShareUtils.putPassword(password);
                    }
                    Toast.makeText(MainActivity.this, "Вход выполнен успешно", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomeAllStoresActivity.class));
                    MainActivity.this.finish();
                } else {
                    Toast.makeText(MainActivity.this, "Ошибка входа, неверное имя пользователя или пароль", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.M_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent); // Переход на экран регистрации
                break;
        }
    }

}
