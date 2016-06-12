package com.example.testapplication.activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.testapplication.fragments.SavedTimersFragment;
import com.example.testapplication.fragments.StopwatchFragment;
import com.example.testapplication.R;
import com.example.testapplication.fragments.TimerFragment;
import com.example.testapplication.helpers.SharedPreferencesHelper;

import java.util.List;
import java.util.Vector;

public class StopwatchActivity extends BaseActivity implements StopwatchFragment.OnFragmentStopwatchStartListener, TimerFragment.OnFragmentTimerStartListener {

    private LinearLayout myLL;
    private int backColor;
    private SharedPreferencesHelper prefs;
    private final static String COLOR_SELECTED = "colorSelected";
    private String tabSecTitle, tabTimerTitle, fragStopwatchTag, fragTimerTag;
    private StopwatchFragment myFragStopwatch;
    private TimerFragment myFragTimer;
    private Vector<Fragment> fragments;
    private ViewPager viewPager;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        initViews();
        initValues();
        initBackColor();
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
        if (data == null) return;
        backColor = data.getIntExtra(COLOR_SELECTED, Color.WHITE); // получаем новый цвет от activity настроек
        changeBackColor(backColor);  // меняем цвет при возвращении с activity настроек
    }

    @Override
    public void onFragmentStopwatchStart() {
        FragmentManager fManagerS = getSupportFragmentManager();
        TimerFragment fragT = (TimerFragment) fManagerS.findFragmentByTag(fragTimerTag);
        if (fragT != null) {
            fragT.stopButton();
        }
    }

    @Override
    public void onFragmentTimerStart() {
        FragmentManager fManagerT = getSupportFragmentManager();
        StopwatchFragment fragS = (StopwatchFragment) fManagerT.findFragmentByTag(fragStopwatchTag);
        if (fragS != null) {
            fragS.stopButton();
        }
    }

    @Override
    public void showSavedTimers() {
        SavedTimersFragment stFrag = new SavedTimersFragment();
    }

    private void changeBackColor(int newBackColor) {
        int oldBackColor = prefs.getSavedColor();
        if (!(oldBackColor == newBackColor)) { // если выбранный цвет фона отличный от текущего
            paintBackground(newBackColor);          // красим фон в новый цвет
            prefs.putSavedColor(newBackColor); // и сохраняем выбранный цвет фона
        }
    }

    private void paintBackground(int backColor) {
        final int oldColor = prefs.getSavedColor(); // старый цвет фона
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
            if (position == 0) return tabSecTitle;
            if (position == 1) return tabTimerTitle;
            return tabSecTitle;
        }

    }

    @Override
    protected void initViews() {
        myLL = (LinearLayout) findViewById(R.id.stopwatchBckground);
        prefs = new SharedPreferencesHelper(this);
        fragStopwatchTag = getFragmentTag(0);
        fragTimerTag = getFragmentTag(1);
        myFragStopwatch = new StopwatchFragment();
        myFragTimer = new TimerFragment();
        fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, myFragStopwatch.getClass().getName()));
        fragments.add(Fragment.instantiate(this, myFragTimer.getClass().getName()));
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(fragments.size());
        PagerAdapter pAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pAdapter);
    }

    @Override
    protected void initValues() {
        backColor = Color.WHITE;
        tabSecTitle = getString(R.string.tabSecTitle);
        tabTimerTitle = getString(R.string.tabTimerTitle);
        tabSecTitle = getString(R.string.tabSecTitle);
        tabTimerTitle = getString(R.string.tabTimerTitle);
    }

    private void initBackColor() {
        if (!(prefs.getSavedColor() == Color.WHITE)) { // если цвет у фона уже был выбран однажды, красим фон в него
            paintBackground(prefs.getSavedColor());
        } else {
            prefs.putSavedColor(backColor);       // иначе, если приложение запущено первый раз, красим фон в белый
            paintBackground(backColor);
        }
    }
}

