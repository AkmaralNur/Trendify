package com.example.shoppingmallsystem.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.shoppingmallsystem.R;
import com.example.shoppingmallsystem.adapter.OrderAdapter;
import com.example.shoppingmallsystem.adapter.PayRVAdapter;
import com.example.shoppingmallsystem.bean.GoodsArrayBean;
import com.example.shoppingmallsystem.bean.OrderBean;
import com.example.shoppingmallsystem.util.AppContext;
import com.example.shoppingmallsystem.util.MySQLiteHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;


/**
 * Экран с деталями заказа
 */
public class OrderDetailsActivity extends AppCompatActivity {

    private OrderBean orderBean;
    private Gson gson;
    private List<GoodsArrayBean.ItemR> goodsData;
    private TextView tv_bar_title;
    private Toolbar toolbar;
    private RecyclerView rv_orderDetails;
    private PayRVAdapter payRVAdapter;
    private TextView tv_total;
    private double total = 0;
    private BigDecimal b1;
    private BigDecimal b2;
    private BigDecimal b3;
    private BigDecimal result;
    private BigDecimal one;
    private double a;
    private TextView tv_orderDetails_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        initData();
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

    private void initData() {
        orderBean = (OrderBean) getIntent().getSerializableExtra("orderDetails");
        gson = new Gson();
        Type type = new TypeToken<List<GoodsArrayBean.ItemR>>() {}.getType();
        goodsData = gson.fromJson(orderBean.getGoodsJson(), type);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tv_bar_title = findViewById(R.id.tv_bar_title);
        tv_bar_title.setText("Детали заказа");
        tv_total = findViewById(R.id.tv_order_total);
        tv_orderDetails_time = findViewById(R.id.tv_orderDetails_time);
        tv_orderDetails_time.setText(orderBean.getTime());
        rv_orderDetails = findViewById(R.id.rv_orderDetails);
        // Инициализация recyclerView
        payRVAdapter = new PayRVAdapter(goodsData);
        rv_orderDetails.setLayoutManager(new LinearLayoutManager(AppContext.getInstance()));
        rv_orderDetails.setAdapter(payRVAdapter);

        /**
         * Проход по данным, чтобы рассчитать общую стоимость
         */
        for (int i = 0; i < goodsData.size(); i++) {
            // Решение проблемы с потерей точности при вычислениях с типом double
            b1 = new BigDecimal(goodsData.get(i).getPrice().trim());
            b2 = new BigDecimal(goodsData.get(i).getNumber());
            b3 = new BigDecimal(total);
            result = b1.multiply(b2);
            result = result.add(b3);
            one = new BigDecimal("1");
            a = result.divide(one, 2, BigDecimal.ROUND_HALF_UP).doubleValue(); // Округление до 2 знаков
            total = a;
        }

        tv_total.setText(total+"сом");
    }
}
