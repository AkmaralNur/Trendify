package com.example.shoppingmallsystem.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.shoppingmallsystem.R;
import com.example.shoppingmallsystem.adapter.MyTabAdapter;
import com.example.shoppingmallsystem.bean.StoreBean;
import com.example.shoppingmallsystem.fragment.StoreCommentFragment;
import com.example.shoppingmallsystem.fragment.StoreGoodsFragment;
import com.example.shoppingmallsystem.fragment.StoreIntroFragment;
import com.example.shoppingmallsystem.util.MySQLiteHelper;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;


/**
 * Экран товаров магазина, открывается после клика по магазину
 */
public class StoreGoodsActivity extends AppCompatActivity {

    private TabLayout tabLayout = null;
    private MyTabAdapter myTabAdapter;
    private ViewPager home_news_viewPager;
    private StoreGoodsFragment storeGoodsFragment;
    private StoreCommentFragment storeCommentFragment;
    private StoreIntroFragment storeIntroFragment;
    private List<Fragment> fragments = new ArrayList<>();

    private TextView tv_bar_title;
    private Toolbar toolbar;
    private String storeID;
    private StoreBean storeBean;
    private ImageView iv_pic;
    private TextView tv_storeName;
    private TextView tv_storeScore;
    private TextView tv_storeSell;
    private TextView tv_storeSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_goods);
        storeID = getIntent().getStringExtra("storeID");
        initData();
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
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initData() {
        storeBean = new StoreBean();
        storeBean = MySQLiteHelper.getInstance(getApplicationContext()).queryStoreBeanFromStoreID(storeID);
        storeGoodsFragment = new StoreGoodsFragment(storeID);
        storeCommentFragment = new StoreCommentFragment();
        storeIntroFragment = new StoreIntroFragment(storeBean);
        fragments.add(storeGoodsFragment);
        fragments.add(storeCommentFragment);
        fragments.add(storeIntroFragment);
    }


    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tv_bar_title = findViewById(R.id.tv_bar_title);
        tv_bar_title.setText("Детали магазина");
        tabLayout = findViewById(R.id.home_newsTab);
        home_news_viewPager = findViewById(R.id.home_news_vp);
        iv_pic = findViewById(R.id.iv_store_act);
        tv_storeName = findViewById(R.id.tv_storeName_act);
        tv_storeScore = findViewById(R.id.tv_store_score_act);
        tv_storeSell = findViewById(R.id.tv_store_sell_act);
        tv_storeSign = findViewById(R.id.tv_store_sign_act);

        // Устанавливаем изображение магазина в зависимости от его ID
        switch (storeBean.getIv_store_pic()){
            case "0":
                iv_pic.setImageResource(R.mipmap.store_1);
                break;
            case "1":
                iv_pic.setImageResource(R.mipmap.store_2);
                break;
            case "2":
                iv_pic.setImageResource(R.mipmap.store_3);
                break;
            case "3":
                iv_pic.setImageResource(R.mipmap.store_4);
                break;
            case "4":
                iv_pic.setImageResource(R.mipmap.store_5);
                break;
            case "5":
                iv_pic.setImageResource(R.mipmap.store_6);
                break;
            case "6":
                iv_pic.setImageResource(R.mipmap.store_7);
                break;
            case "7":
                iv_pic.setImageResource(R.mipmap.store_8);
                break;
        }

        // Устанавливаем данные магазина
        tv_storeName.setText(storeBean.getStoreName());
        tv_storeScore.setText(storeBean.getStoreScore() + " баллов");
        tv_storeSell.setText("Продаж в месяц: " + storeBean.getStoreSell());
        tv_storeSign.setText(storeBean.getStoreSign());

        // Инициализация адаптера для вкладок
        myTabAdapter = new MyTabAdapter(getSupportFragmentManager(), fragments);
        home_news_viewPager.setAdapter(myTabAdapter);
        tabLayout.setupWithViewPager(home_news_viewPager);
    }

}
