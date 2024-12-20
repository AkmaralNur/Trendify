package com.example.shoppingmallsystem.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.shoppingmallsystem.MainActivity;
import com.example.shoppingmallsystem.R;
import com.example.shoppingmallsystem.adapter.PayRVAdapter;
import com.example.shoppingmallsystem.bean.GoodsArrayBean;
import com.example.shoppingmallsystem.bean.OrderBean;
import com.example.shoppingmallsystem.util.AppContext;
import com.example.shoppingmallsystem.util.DateUtill;
import com.example.shoppingmallsystem.util.MySQLiteHelper;
import com.example.shoppingmallsystem.util.ToastUtil;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Платежный экран
 */
public class PayActivity extends AppCompatActivity {

    private TextView tv_bar_title;
    private Toolbar toolbar;

    private List<GoodsArrayBean.ItemR> data = new ArrayList<>();
    private RecyclerView rv_pay;
    private TextView tv_pay_total;
    private TextView tv_submitOrder;
    private PayRVAdapter payRVAdapter;
    private double total = 0;
    private BigDecimal b1;
    private BigDecimal b2;
    private BigDecimal b3;
    private BigDecimal result;
    private BigDecimal one;
    private double a;
    private Dialog dialog;
    private double accountMoney;
    private OrderBean orderBean;
    private Gson gson;
    private String goodsJson;

    private BigDecimal a1;
    private BigDecimal a2;
    private BigDecimal result1;
    private double resultMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        data = (ArrayList<GoodsArrayBean.ItemR>) getIntent().getSerializableExtra("PayList");
        initView();
        setActionBar();
    }

    /* Настройка ActionBar */
    private void setActionBar() {
        setSupportActionBar(toolbar);
        /* Показать иконку Home */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Отключить отображение названия проекта
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    // Установка кнопки "Назад" для toolbar
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        rv_pay = findViewById(R.id.rv_pay);
        tv_pay_total = findViewById(R.id.tv_pay_total);
        tv_submitOrder = findViewById(R.id.tv_submitOrder);
        toolbar = findViewById(R.id.toolbar);
        tv_bar_title = findViewById(R.id.tv_bar_title);
        tv_bar_title.setText("Подтвердите заказ");

        // Инициализация recyclerView
        rv_pay.setItemAnimator(null);
        payRVAdapter = new PayRVAdapter(data);
        rv_pay.setLayoutManager(new LinearLayoutManager(AppContext.getInstance()));
        rv_pay.setAdapter(payRVAdapter);

        /**
         * Проход по данным, чтобы рассчитать общую стоимость
         */
        for (int i = 0; i < data.size(); i++) {
            b1 = new BigDecimal(data.get(i).getPrice().trim());
            b2 = new BigDecimal(data.get(i).getNumber());
            b3 = new BigDecimal(total);
            result = b1.multiply(b2);
            result = result.add(b3);
            one = new BigDecimal("1");
            a = result.divide(one, 2, BigDecimal.ROUND_HALF_UP).doubleValue(); // Округление до 2 знаков
            total = a;
        }

        tv_pay_total.setText(total+"сом");

        tv_submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Подтверждение заказа
                dialog = new AlertDialog.Builder(PayActivity.this).setTitle("Подтвердить покупку?")
                        .setMessage("Общая сумма: " + total)
                        .setIcon(R.drawable.ic_shop)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                doInsertOrder();
                            }
                        })
                        .setNegativeButton("Нет", null)
                        .create();
                dialog.show();
            }
        });
    }


    /**
     * Процесс оформления заказа
     */
    private void doInsertOrder() {
        accountMoney = MySQLiteHelper.getInstance(PayActivity.this).getUserMoneyFromUserName(MainActivity.username);
        // Проверка достаточности средств
        if (total > accountMoney) {
            ToastUtil.showShort("Недостаточно средств на счету, пожалуйста, пополните баланс.");
            dialog.dismiss();
        } else {
            // Решение проблемы с точностью double
            a1 = new BigDecimal(accountMoney);
            a2 = new BigDecimal(total);
            result1 = a1.subtract(a2);
            one = new BigDecimal("1");
            resultMoney = result1.divide(one, 2, BigDecimal.ROUND_HALF_UP).doubleValue(); // Округление до 2 знаков

            MySQLiteHelper.getInstance(PayActivity.this).RechargeMoney(MainActivity.username, resultMoney);
            gson = new Gson();
            goodsJson = gson.toJson(data);
            orderBean = new OrderBean(MainActivity.username, DateUtill.getCurrentTime(), goodsJson);
            MySQLiteHelper.getInstance(PayActivity.this).insertOrderInfo(orderBean);
            ToastUtil.showShort("Заказ успешно оформлен!");
            // Обновление данных в корзине
            PayActivity.this.finish();
        }
    }
}
