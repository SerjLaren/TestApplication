package com.example.testapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.testapplication.R;
import com.example.testapplication.services.TimerService;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public String actCHosen = "";
    private int positionInSpinner;
    private ArrayAdapter<String> arrAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] dataSpinner = {getString(R.string.step1), getString(R.string.step2), getString(R.string.step3)};
        arrAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataSpinner);
        initViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGotoAct:
                if (positionInSpinner == 0) {
                    Intent intent = new Intent(this, StopwatchActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        Button btnGotoAct = (Button) findViewById(R.id.btnGotoAct);
        btnGotoAct.setOnClickListener(this);
        arrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerAct = (Spinner) findViewById(R.id.spinnerAct);
        spinnerAct.setAdapter(arrAdapter);
        spinnerAct.setPrompt("Title");
        spinnerAct.setSelection(0);
        spinnerAct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionInSpinner = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
}
