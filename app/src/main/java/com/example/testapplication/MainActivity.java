package com.example.testapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] dataSpinner = {"Секундомер", "другое...", "другое..."};
    public String actCHosen = "";
    private int positionInSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnGotoAct = (Button) findViewById(R.id.btnGotoAct);
        btnGotoAct.setOnClickListener(this);
        ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataSpinner);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGotoAct:
                if(positionInSpinner == 0)
                {
                  Intent intent = new Intent(this, StopwatchActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }






}
