package com.example.shoppingmallsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoppingmallsystem.MainActivity;
import com.example.shoppingmallsystem.R;
import com.example.shoppingmallsystem.adapter.AllStoresAdapter;
import com.example.shoppingmallsystem.bean.StoreBean;
import com.example.shoppingmallsystem.util.MySQLiteHelper;
import com.example.shoppingmallsystem.util.ShareUtils;
import com.example.shoppingmallsystem.util.ToastUtil;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

public class HomeAllStoresActivity extends AppCompatActivity implements View.OnClickListener {

    /* Создание переключателя для связки DrawerLayout и Toolbar */
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView tv_bar_title;
    private TextView tv_main_userName;
    private ImageView iv_head;

    private AllStoresAdapter allStoresAdapter;
    private RecyclerView rv_stores;
    private List<StoreBean> storeBeans;
    private LinearLayout headerView;
    private int UserID;

    // Инициализация данных
    private void initData() {
        storeBeans = new ArrayList<>();
        storeBeans = MySQLiteHelper.getInstance(getApplicationContext()).queryAllStores();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_all_stores);
        // Загрузка данных
        initData();
        // Инициализация видов
        ininView();
        // Скрытие полосы прокрутки
        hideScrollBar();
        // Установка ActionBar
        setActionBar();
        /* Настройка переключателя DrawerLayout */
        setDrawerToggle();
        /* Установка слушателей */
        setListener();
    }

    /* Инициализация видов */
    private void ininView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);
        tv_bar_title = findViewById(R.id.tv_bar_title);
        tv_bar_title.setText("Список магазинов");

        // Получаем заголовок
        headerView = (LinearLayout) navigationView.getHeaderView(0);
        tv_main_userName = headerView.findViewById(R.id.tv_header_userName);
        iv_head = headerView.findViewById(R.id.iv_head);
        tv_main_userName.setText(MainActivity.username);
        UserID = MySQLiteHelper.getInstance(getApplicationContext()).GetUserId(MainActivity.username);
        switch (UserID) {
            case 1:
                iv_head.setImageResource(R.drawable.tx_1_48);
                break;
            case 2:
                iv_head.setImageResource(R.drawable.tx_2_48);
                break;
            case 3:
                iv_head.setImageResource(R.drawable.tx_3_48);
                break;
            case 4:
                iv_head.setImageResource(R.drawable.tx_4_48);
                break;
            case 5:
                iv_head.setImageResource(R.drawable.tx_5_48);
                break;
            case 6:
                iv_head.setImageResource(R.drawable.tx_6_48);
                break;
        }

        rv_stores = findViewById(R.id.rv_stores);
        allStoresAdapter = new AllStoresAdapter(storeBeans);
        // Инициализация RecyclerView
        rv_stores.setItemAnimator(new DefaultItemAnimator());
        rv_stores.setLayoutManager(new LinearLayoutManager(this));
        rv_stores.setAdapter(allStoresAdapter);

        // Установка обработчика кликов для элементов
        allStoresAdapter.setOnItemClickListener(new AllStoresAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position, StoreBean storeBean) {
                startActivity(new Intent(HomeAllStoresActivity.this, StoreGoodsActivity.class).putExtra("storeID", storeBean.getID()));
            }
        });
    }

    /* Скрыть полосу прокрутки в навигационном меню */
    private void hideScrollBar() {
        navigationView.getChildAt(0).setVerticalScrollBarEnabled(false);
    }

    /* Установка ActionBar */
    private void setActionBar() {
        setSupportActionBar(toolbar);
        /* Показать иконку Home */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Отключить отображение названия
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /* Установка переключателя для DrawerLayout и связывание его с иконкой Home */
    private void setDrawerToggle() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(toggle);
        /* Синхронизация состояния DrawerLayout */
        toggle.syncState();
    }

    /* Установка слушателей для навигационного меню */
    private void setListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.single_1:
                        startActivity(new Intent(HomeAllStoresActivity.this, OrderActivity.class));
                        break;
                    case R.id.single_2:
                        startActivity(new Intent(HomeAllStoresActivity.this, MyAccountActivity.class));
                        break;
                    case R.id.single_3:
                        startActivity(new Intent(HomeAllStoresActivity.this, PersonalCenterActivity.class));
                        break;
                    case R.id.single_4:
                        startActivity(new Intent(HomeAllStoresActivity.this, MainActivity.class));
                        ShareUtils.putAuto_Login("0");
                        HomeAllStoresActivity.this.finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    /**
     * Метод для выхода из приложения при двойном нажатии на кнопку "Назад"
     */
    protected long exitTime; // Время первого нажатия
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(HomeAllStoresActivity.this, "Нажмите еще раз для выхода из приложения",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                HomeAllStoresActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
