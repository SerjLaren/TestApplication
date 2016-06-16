package com.example.testapplication.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.testapplication.R;
import com.example.testapplication.helpers.SharedPreferencesHelper;

public class ApiImageFragment extends Fragment implements View.OnClickListener {

    private Button btnSetBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_api_image, container, false);
        initValues();
        initViews(v);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSetBackImage:
                // Установить картинку фоном
                Toast toast = Toast.makeText(getActivity(),
                        "Фон установлен", Toast.LENGTH_SHORT);
                toast.show();
                break;
            default:
                break;
        }
    }

    private void initViews(View v) {
        //Добавить картинку на фрагмент
        btnSetBack = (Button) v.findViewById(R.id.btnSetBackImage);
        btnSetBack.setOnClickListener(this);
    }

    private void initValues() {

    }
}
