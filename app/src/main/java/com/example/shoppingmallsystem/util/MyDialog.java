package com.example.shoppingmallsystem.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.shoppingmallsystem.R;
import com.example.shoppingmallsystem.activity.PayActivity;
import com.example.shoppingmallsystem.adapter.ShoppongCarGoodsAdapter;
import com.example.shoppingmallsystem.bean.GoodsArrayBean;
import com.example.shoppingmallsystem.fragment.StoreGoodsFragment;

import java.io.Serializable;
import java.util.List;


public class MyDialog {
    private static CustomDialog dialog;
    private View view; // макет диалога
    private View animView; // макет меню товаров
    private View dismissView; // полупрозрачный оверлей
    private RelativeLayout re_bottow ;
    private RecyclerView recyclerView_carGoods;
    private TextView tv_clean ;
    private TextView tv_DoShopping;
    private ImageView iv_shoppingCar ;
    private Context context;
    private ShoppongCarGoodsAdapter shoppongCarGoodsAdapter;
    public static TextView tv_total;
    public static  double total = 0;
    private Intent intent ;


    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    dialog.dismiss();
                    break;
                case 2:
                    for (int i = 0;i< StoreGoodsFragment.goodsArrayBean.itemsRight.size() ;i++){
                        StoreGoodsFragment.goodsArrayBean.itemsRight.get(i).setNumber(0);
                    }
                    StoreGoodsFragment.handler.sendEmptyMessage(1);
                    dialog.dismiss();
            }
        }
    };


    public MyDialog(final Context context, final List<GoodsArrayBean.ItemR> dialogData) {
        this.context = context;
        dialog = new CustomDialog(context, R.style.myDialog);
        // стиль диалога
        view = LayoutInflater.from(context).inflate(R.layout.shoppcar_dialog, null);
        // получаем элементы диалога
        animView = view.findViewById(R.id.v_anim);
        re_bottow = view.findViewById(R.id.rl);
        tv_clean = view.findViewById(R.id.tv_car_clean);
        recyclerView_carGoods = view.findViewById(R.id.re_shoppingGoods);
        tv_DoShopping = view.findViewById(R.id.tv_DoShopping);
        iv_shoppingCar = view.findViewById(R.id.iv_shoppingCar);
        tv_total =view.findViewById(R.id.tv_total_dialog);

        total = StoreGoodsFragment.total;
        tv_total.setText("$ "+StoreGoodsFragment.total);
        iv_shoppingCar.setImageResource(R.drawable.shoppingcar_full_64);
        tv_DoShopping.setText("Перейти к оплате");
        // инициализация recyclerView
        recyclerView_carGoods.setItemAnimator(null);
        shoppongCarGoodsAdapter = new ShoppongCarGoodsAdapter(dialogData);
        recyclerView_carGoods.setLayoutManager(new LinearLayoutManager(AppContext.getInstance()));
        recyclerView_carGoods.setAdapter(shoppongCarGoodsAdapter);


        dialog.setContentView(view);

        dismissView = view.findViewById(R.id.v_dimiss);
        dismissView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        re_bottow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        tv_DoShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent();
                intent.setClass(context, PayActivity.class);
                intent.putExtra("PayList",(Serializable) dialogData);
                context.startActivity(intent);
            }
        });

        tv_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogData.clear();
                shoppongCarGoodsAdapter.notifyDataSetChanged();
                for (int i = 0;i< StoreGoodsFragment.goodsArrayBean.itemsRight.size() ;i++){
                    StoreGoodsFragment.goodsArrayBean.itemsRight.get(i).setNumber(0);
                }
                StoreGoodsFragment.handler.sendEmptyMessage(1);
                dialog.dismiss();
                ToastUtil.showShort("Корзина очищена");
            }
        });

        dialog.setBeforeDismiss(new CustomDialog.IBeforeDismiss() {
            @Override
            public void onBeforeDismiss() {
                dismissAnim();
            }
        });


        // установка позиции диалога
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent); // прозрачный фон, иначе будет белое пятно
//        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); // без анимации окна
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // ширина
        lp.height = WindowManager.LayoutParams.MATCH_PARENT; // высота
        dialogWindow.setAttributes(lp);
        // установка диалога внизу экрана
        dialogWindow.setGravity(Gravity.BOTTOM);
    }




    public void show() {
        dialog.show();
        showAnim();
    }

    // анимация появления
    private void showAnim() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, DensityUtil.dp2px(context, 300), 0);
        animation.setDuration(300);
        animView.startAnimation(animation);
    }

    // анимация исчезновения
    private void dismissAnim() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, DensityUtil.dp2px(context, 300));
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dialog.myDismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setDuration(300);
        animView.startAnimation(animation);
    }

}
