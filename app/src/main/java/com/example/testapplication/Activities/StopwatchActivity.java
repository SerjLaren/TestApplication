package com.example.testapplication.activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.testapplication.fragments.FragmentSeconds;
import com.example.testapplication.R;
import com.example.testapplication.fragments.FragmentTimer;

import java.util.List;
import java.util.Vector;

public class StopwatchActivity extends AppCompatActivity implements FragmentSeconds.OnFragmentSecStartListener, FragmentTimer.OnFragmentTimerStartListener {

    private LinearLayout myLL;
    private int backColor;
    private SharedPreferences sPref;
    private SharedPreferences.Editor edit;
    private String SAVED_COLOR, COLOR_SELECTED, tabSecTitle, tabTimerTitle, fragSecTag, fragTimerTag;
    private FragmentSeconds myFragSec;
    private FragmentTimer myFragTimer;
    private Vector<Fragment> fragments;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        backColor = Color.WHITE;
        myLL = (LinearLayout) findViewById(R.id.stopwatchBckground);
        SAVED_COLOR = "back_color";
        COLOR_SELECTED = "colorSelected";
        tabSecTitle = getString(R.string.tabSecTitle);
        tabTimerTitle = getString(R.string.tabTimerTitle);
        sPref = getPreferences(MODE_PRIVATE);
        edit = sPref.edit();
        fragSecTag = getFragmentTag(0);
        fragTimerTag = getFragmentTag(1);
        tabSecTitle = getString(R.string.tabSecTitle);
        tabTimerTitle = getString(R.string.tabTimerTitle);
        myFragSec = new FragmentSeconds();
        myFragTimer = new FragmentTimer();
        fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, myFragSec.getClass().getName()));
        fragments.add(Fragment.instantiate(this, myFragTimer.getClass().getName()));
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(fragments.size());
        PagerAdapter pAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pAdapter);
        if (!(sPref.getInt(SAVED_COLOR, Color.WHITE) == Color.WHITE)) // если цвет у фона уже был выбран однажды, красим фон в него
            paintBackground(sPref.getInt(SAVED_COLOR, Color.WHITE)); else {
            edit.putInt(SAVED_COLOR, backColor);
            edit.commit();                           // иначе, если приложение запущено первый раз, красим фон в белый
            paintBackground(backColor);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stopwatch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.secondsSettings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        backColor = data.getIntExtra(COLOR_SELECTED, Color.WHITE); // получаем новый цвет от activity настроек
        changeBackColor(backColor);  // меняем цвет при возвращении с activity настроек
    }

    @Override
    public void onFragmentSecStart() {
        FragmentManager fManagerS = getSupportFragmentManager();
        FragmentTimer fragT = (FragmentTimer) fManagerS.findFragmentByTag(fragTimerTag);
        if(fragT != null)
            fragT.stopButton();
    }

    @Override
    public void onFragmentTimerStart() {
        FragmentManager fManagerT = getSupportFragmentManager();
        FragmentSeconds fragS = (FragmentSeconds) fManagerT.findFragmentByTag(fragSecTag);
        if(fragS != null)
            fragS.stopButton();
    }

    private void changeBackColor(int newBackColor) {
        int oldBackColor = sPref.getInt(SAVED_COLOR, Color.WHITE);
        if (!(oldBackColor == newBackColor)) { // если выбранный цвет фона отличный от текущего
            paintBackground(newBackColor);          // красим фон в новый цвет
            edit.putInt(SAVED_COLOR, newBackColor); // и сохраняем выбранный цвет фона
            edit.commit();
        }
    }

    private void paintBackground(int backColor) {
        final int oldColor = (sPref.getInt(SAVED_COLOR, Color.WHITE)); // старый цвет фона
        final int finalColor = (backColor);  // новый цвет фона

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1); // аниматор для смены цвета
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float position = animation.getAnimatedFraction();
                int blended = blendColors(oldColor, finalColor, position);  // смешиваем от старого к новому цвету
                myLL.setBackgroundColor(blended);  // красим экран цветами
            }
        });
        anim.setDuration(3000);  // анимация 3 секунды
        anim.start();
    }

    private int blendColors(int from, int to, float ratio) { // функция смешивания цветов для анимации
        final float inverseRatio = 1f - ratio;
        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio; // смешивание R старого и нового цвета
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;  // смешивание G старого и нового цвета
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio; // смешивание B старого и нового цвета
        return Color.rgb((int) r, (int) g, (int) b); // полученное смешивание
    }

    public String getFragmentTag(int pos){
        return "android:switcher:"+R.id.pager+":"+pos;
    }

    class PagerAdapter extends FragmentPagerAdapter {  // Адаптер для ViewPager

        private List<Fragment> fragments;

        public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0)
                return tabSecTitle;
            if (position == 1)
                return tabTimerTitle;
            return tabSecTitle;
        }

    }
}

