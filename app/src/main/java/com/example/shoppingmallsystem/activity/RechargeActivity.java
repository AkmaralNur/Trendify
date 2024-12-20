package com.example.shoppingmallsystem.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.shoppingmallsystem.MainActivity;
import com.example.shoppingmallsystem.R;
import com.example.shoppingmallsystem.util.MySQLiteHelper;
import com.example.shoppingmallsystem.util.ToastUtil;


/**
 * Активити для пополнения счета
 */
public class RechargeActivity extends AppCompatActivity {

    private TextView tv_bar_title;
    private Toolbar toolbar;
    private EditText et_rechargeMoney;
    private Button btn_doRecharge;
    private String money1;
    private double currentAccountMoney;
    private AlertDialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initView();
        setActionBar();
    }

    /*Настройка ActionBar*/
    private void setActionBar() {
        setSupportActionBar(toolbar);
        /*Показать значок Home*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Отключить отображение названия проекта
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    //Настройка кнопки возврата для toolbar
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            setResult(1);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        //Получение текущего баланса аккаунта
        currentAccountMoney = MySQLiteHelper.getInstance(getApplicationContext()).getUserMoneyFromUserName(MainActivity.username);

        toolbar = findViewById(R.id.toolbar);
        tv_bar_title = findViewById(R.id.tv_bar_title);
        tv_bar_title.setText("Пополнение");
        et_rechargeMoney = findViewById(R.id.et_rechargeMoney);
        btn_doRecharge = findViewById(R.id.btn_doRecharge);

        //Обработчик события нажатия для пополнения счета
        btn_doRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                money1 = et_rechargeMoney.getText().toString();
                if (money1.equals("0")) {
                    ToastUtil.showShort("Сумма не может быть 0");
                } else if (TextUtils.isEmpty(money1)) {
                    ToastUtil.showShort("Сумма не может быть пустой");
                } else {
                    double m1 = Double.parseDouble(money1);
                    currentAccountMoney = currentAccountMoney + m1;
                    dialog1 = new AlertDialog.Builder(RechargeActivity.this)
                            .setTitle("Подтвердить пополнение")
                            .setMessage("Пополнение счета: " + MainActivity.username + '\n' + "Сумма: " + money1 + " сом")
                            .setIcon(R.drawable.ic_money)
                            .setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MySQLiteHelper.getInstance(getBaseContext()).RechargeMoney(MainActivity.username, currentAccountMoney);
                                    ToastUtil.showShort("Пополнение прошло успешно");
                                    et_rechargeMoney.setText("");
                                }
                            })
                            .setNegativeButton("Отменить", null)
                            .create();
                    dialog1.show();
                }
            }
        });
    }
}
