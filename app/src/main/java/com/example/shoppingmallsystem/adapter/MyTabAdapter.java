package com.example.shoppingmallsystem.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер для вкладок (tabs)
 */
public class MyTabAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    String[] Title = new String[]{"Заказ", "Обсуждение", "Продавец"};

    public MyTabAdapter(@NonNull FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    /**
     * Этот метод используется для отображения названий вкладок
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return Title[position];
    }
}
