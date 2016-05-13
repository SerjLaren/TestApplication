package com.example.testapplication;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StopwatchActivity extends AppCompatActivity implements View.OnClickListener {

    private int second, minut;
    private TextView tvSeconds, tvMinuts;
    private LinearLayout myLL;
    private String strMinut = "", strSecond = "", titleMinuts = "";
    private boolean timerStoped = true;
    private String backColor, SECONDS, MINUTS;
    private SharedPreferences sPref;
    private SharedPreferences.Editor edit;
    private BroadcastReceiver br;
    public final static String CONNECTION_TO_TIMERSERVICE = "com.example.develop.stopwatch_timerservice";
    private String SAVED_COLOR, COLOR_SELECTED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stopwatch);
        tvSeconds = (TextView) findViewById(R.id.textviewSeconds);
        tvMinuts = (TextView) findViewById(R.id.textviewMinuts);
        strMinut = Integer.toString(minut);
        strSecond = Integer.toString(second);
        tvMinuts.setText("");
        tvSeconds.setText("");
        backColor = getString(R.string.color1);
        myLL = (LinearLayout) findViewById(R.id.stopwatchBckground);
        titleMinuts = getString(R.string.minutsStr);
        SAVED_COLOR = getString(R.string.SAVED_COLOR);
        COLOR_SELECTED = getString(R.string.COLOR_SELECTED);
        SECONDS = getString(R.string.SECONDS);
        MINUTS = getString(R.string.MINUTS);
        sPref = getPreferences(MODE_PRIVATE);
        edit = sPref.edit();

        if (!((sPref.getString(SAVED_COLOR, "")).equals(""))) // если цвет у фона уже был выбран однажды, красим фон в него
            paintBackground(sPref.getString(SAVED_COLOR, "")); else {
            edit.putString(SAVED_COLOR, backColor);
            edit.commit();                           // иначе, если приложение запущено первый раз, красим фон в белый
            paintBackground(backColor);
        }

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) { // действия при получении данных от сервиса
                timerStoped = false;
                second = intent.getIntExtra(SECONDS, 0);
                minut = intent.getIntExtra(MINUTS, 0);
                strMinut = Integer.toString(minut);
                strSecond = Integer.toString(second);
                tvMinuts.setText(titleMinuts + strMinut);
                tvSeconds.setText(strSecond);
            }
        };
        IntentFilter intFilt = new IntentFilter(CONNECTION_TO_TIMERSERVICE);
        registerReceiver(br, intFilt);
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
            case R.id.secondsRestart:
                if(timerStoped) {
                    startService(new Intent(this, TimerService.class)); // запускаем сервис
                    timerStoped = false;
                }
                return true;
            case R.id.secondsStop:
                stopService(new Intent(this, TimerService.class)); // останавливаем сервис
                timerStoped = true;
                return true;
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
        backColor = data.getStringExtra(COLOR_SELECTED); // получаем новый цвет от activity настроек
        changeBackColor(backColor);  // меняем цвет при возвращении с activity настроек
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br); // выключение broadcastresiever
    }

    protected void onSaveInstanceState(Bundle outState) { // эти методы все-таки необходимо оставить
        super.onSaveInstanceState(outState);
        outState.putInt(SECONDS, second);
        outState.putInt(MINUTS, minut);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) { // эти методы все-таки необходимо оставить
        super.onRestoreInstanceState(savedInstanceState);
        second = savedInstanceState.getInt(SECONDS);
        minut = savedInstanceState.getInt(MINUTS);
    }

    private void changeBackColor(String newBackColor) {
        String oldBackColor = sPref.getString(SAVED_COLOR, "");
        if (!oldBackColor.equals(newBackColor)) { // если выбранный цвет фона отличный от текущего
            paintBackground(newBackColor);          // красим фон в новый цвет
            edit.putString(SAVED_COLOR, newBackColor); // и сохраняем выбранный цвет фона
            edit.commit();
        }
    }

    private void paintBackground(String backColor) {
        final int oldColor = Color.parseColor(sPref.getString(SAVED_COLOR, "")); // старый цвет фона
        final int finalColor = Color.parseColor(backColor);  // новый цвет фона

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
}
