package com.example.testapplication.Activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.testapplication.Fragments.FragmentSeconds;
import com.example.testapplication.R;

public class StopwatchActivity extends AppCompatActivity {

    private LinearLayout myLL;
    private String backColor;
    private SharedPreferences sPref;
    private SharedPreferences.Editor edit;
    private String SAVED_COLOR, COLOR_SELECTED;
    Fragment myFrag;
    FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stopwatch);
        backColor = "#FFFFFF";
        myLL = (LinearLayout) findViewById(R.id.stopwatchBckground);
        SAVED_COLOR = "back_color";
        COLOR_SELECTED = "colorSelected";
        sPref = getPreferences(MODE_PRIVATE);
        edit = sPref.edit();

        myFrag = new FragmentSeconds();
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_fragment, myFrag);
        ft.commit();

        if (!((sPref.getString(SAVED_COLOR, "")).equals(""))) // если цвет у фона уже был выбран однажды, красим фон в него
            paintBackground(sPref.getString(SAVED_COLOR, "")); else {
            edit.putString(SAVED_COLOR, backColor);
            edit.commit();                           // иначе, если приложение запущено первый раз, красим фон в белый
            paintBackground(backColor);
        }
    }

    //-------------------------------------------------------------------------------------------------


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
        backColor = data.getStringExtra(COLOR_SELECTED); // получаем новый цвет от activity настроек
        changeBackColor(backColor);  // меняем цвет при возвращении с activity настроек
    }

    /*-------------- Методы работы с цветом фона ----------------------------------------------------------------------*/

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
