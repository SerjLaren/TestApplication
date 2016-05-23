package com.example.testapplication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.testapplication.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnColor1, btnColor2, btnColor3, btnColor4, btnColor5, btnColor6;
    private String color1, color2, color3, color4, color5, color6;
    private final static String COLOR_SELECTED = "colorSelected";
    public Intent returnIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        color1 = getString(R.string.color1);
        color2 = getString(R.string.color2);
        color3 = getString(R.string.color3);
        color4 = getString(R.string.color4);
        color5 = getString(R.string.color5);
        color6 = getString(R.string.color6);
        btnColor1 = (Button) findViewById(R.id.btnColor1); // Белый
        btnColor1.setOnClickListener(this);
        btnColor2 = (Button) findViewById(R.id.btnColor2); // Зеленый
        btnColor2.setOnClickListener(this);
        btnColor3 = (Button) findViewById(R.id.btnColor3); // Желтый
        btnColor3.setOnClickListener(this);
        btnColor4 = (Button) findViewById(R.id.btnColor4); // Синий
        btnColor4.setOnClickListener(this);
        btnColor5 = (Button) findViewById(R.id.btnColor5); // Красный
        btnColor5.setOnClickListener(this);
        btnColor6 = (Button) findViewById(R.id.btnColor6); // Голубой
        btnColor6.setOnClickListener(this);

        btnColor1.setBackgroundColor(Color.parseColor(color1));
        btnColor2.setBackgroundColor(Color.parseColor(color2));
        btnColor3.setBackgroundColor(Color.parseColor(color3));
        btnColor4.setBackgroundColor(Color.parseColor(color4));
        btnColor5.setBackgroundColor(Color.parseColor(color5));
        btnColor6.setBackgroundColor(Color.parseColor(color6));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnColor1:
                returnIntent.putExtra(COLOR_SELECTED, color1);
                setResult(RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.btnColor2:
                returnIntent.putExtra(COLOR_SELECTED, color2);
                setResult(RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.btnColor3:
                returnIntent.putExtra(COLOR_SELECTED, color3);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.btnColor4:
                returnIntent.putExtra(COLOR_SELECTED, color4);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.btnColor5:
                returnIntent.putExtra(COLOR_SELECTED, color5);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.btnColor6:
                returnIntent.putExtra(COLOR_SELECTED, color6);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
            default:
                break;
        }
    }
}
