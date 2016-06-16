package com.example.testapplication.activities;

import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.testapplication.R;
import com.example.testapplication.adapters.RecyclerApiAdapter;
import com.example.testapplication.fragments.ApiImageFragment;

import java.util.ArrayList;

public class ApiActivity extends BaseActivity implements RecyclerApiAdapter.onRVItemClickListener {

    private RecyclerView myRecyclerView;
    private RecyclerApiAdapter myAdapter;
    private ArrayList data;
    private RecyclerView.LayoutManager lm;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ApiImageFragment imageFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        initValues();
        initViews();
    }

    @Override
    protected void initViews() {
        myRecyclerView = (RecyclerView) findViewById(R.id.rv_oboobs);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            lm = new GridLayoutManager(this, 2);
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            lm = new GridLayoutManager(this, 4);
        myRecyclerView.setLayoutManager(lm);
        myAdapter = new RecyclerApiAdapter(this, data, myRecyclerView);
        myRecyclerView.setAdapter(myAdapter);
        myRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void initValues() {
        data = new ArrayList();
        for(int i = 0; i < 100; i++) {
            data.add("Тут будет картинка " + String.valueOf(i)); //выгрузить картинки в массив
        }
    }

    @Override
    public void onItemClick(int position) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_image, R.anim.slide_out);
        if (imageFrag == null) {
            imageFrag = new ApiImageFragment();
            ft.replace(R.id.fl_api_rv, imageFrag);  //надо еще работать с позицией элемента
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
